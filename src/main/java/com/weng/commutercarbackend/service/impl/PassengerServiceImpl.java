package com.weng.commutercarbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.weng.commutercarbackend.common.ResultCodeEnum;
import com.weng.commutercarbackend.exception.BusinessException;
import com.weng.commutercarbackend.mapper.DriverMapper;
import com.weng.commutercarbackend.mapper.PassengerMapper;
import com.weng.commutercarbackend.model.dto.LoginRequest;
import com.weng.commutercarbackend.model.dto.RegisterRequest;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.entity.Stop;
import com.weng.commutercarbackend.model.vo.LoginVO;
import com.weng.commutercarbackend.service.PassengerService;
import com.weng.commutercarbackend.utils.JwtUtil;
import com.weng.commutercarbackend.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
* @author weng
* @description 针对表【passenger(乘客表)】的数据库操作Service实现
* @createDate 2024-03-30 18:30:30
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger>
    implements PassengerService {

    private final PassengerMapper passengerMapper;
    private final DriverMapper driverMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate stringRedisTemplate;
    private final WebSocketServer webSocketServer;
    private final Gson gson;

    @Override
    public LoginVO login(LoginRequest loginRequest) {
        //1.根据用户名密码进行登录
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //注：如果查询到了driver，那么这里会报错，因为driver不是passenger。所以登陆时传的role很重要
        Passenger passenger = (Passenger) authenticationResponse.getPrincipal();
        //2.生成jwt令牌
        String token = jwtUtil.generateToken(passenger);
        //3.返回用户信息
        return LoginVO.builder()
                .id(passenger.getId())
                .username(passenger.getUsername())
                .name(passenger.getName())
                .phone(passenger.getPhone())
                .token(token)
                .build();
    }

    @Override
    public Long register(RegisterRequest registerRequest) {
        //1.密码和校验密码相同
        if (!Objects.equals(registerRequest.password(), registerRequest.checkPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "两次密码输入不一致");
        }
        //2.账号不能重复(查数据库)
        LambdaQueryWrapper<Passenger> passengerLambdaQueryWrapper = new LambdaQueryWrapper<>();
        passengerLambdaQueryWrapper.eq(Passenger::getUsername, registerRequest.username());
        Long count = passengerMapper.selectCount(passengerLambdaQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "账号重复");
        }
        //3.存储到数据库
        Passenger passenger = Passenger.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .phone(registerRequest.phone())
                .build();
        passengerMapper.insert(passenger);//如果插入失败，它会抛出异常.而不是返回一个负数

        return passenger.getId();
    }

    /**
     * todo 核心代码：人车拟合
     * @param id
     */
    @Override
    public void compareLocation(Long id) throws IOException {
        //1.获取当前乘客的时间
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        Set<String> passengerTimes = hashOperations.keys("passenger_" + id);
        //2.遍历每个司机
        List<Driver> drivers = driverMapper.selectList(null);
        for (Driver driver : drivers) {
            //3.获取当前遍历司机的时间
            Set<String> driverTimes = hashOperations.keys("driver_" + driver.getId());
            //4.比对,如果10次都比对成功,则匹配成功
            int count=0;
            for (String passengerTime : passengerTimes) {
                for (String driverTime : driverTimes) {
                    if (passengerTime.equals(driverTime)) {
                        String passengerLocation = hashOperations.get("passenger_" + id, passengerTime);
                        String driverLocation = hashOperations.get("driver_" + driver.getId(), driverTime);
                        Boolean result = compare(passengerLocation, driverLocation);
                        if (result) {
                            count++;
                        }
                    }
                }
            }
            //5.如果匹配成功,则向前端传递数据。并进行考勤和扣费
            if (count==10){
                Map<String,Object> map=new HashMap<>();
                map.put("type", 1);//消息类型，1表示人车拟合成功
                map.put("message", "系统检测您已在车上,按确定进行考勤和扣费");
                webSocketServer.sendToUser("passenger_"+id,gson.toJson(map));
                //考勤和扣费
                LambdaUpdateWrapper<Passenger> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.set(Passenger::getDriverId,driver.getId());
                lambdaUpdateWrapper.setSql("money = money - 5");
                lambdaUpdateWrapper.set(Passenger::getUpdateTime, LocalDateTime.now());
                passengerMapper.update(lambdaUpdateWrapper);
            }
        }
    }

    private Boolean compare(String passengerLocation, String driverLocation) {
        String[] passengerLocationArray = passengerLocation.split(",");
        String[] driverLocationArray = driverLocation.split(",");
        double passengerLatitude = Double.parseDouble(passengerLocationArray[0]);
        double passengerLongitude = Double.parseDouble(passengerLocationArray[1]);
        double passengerSpeed = Double.parseDouble(passengerLocationArray[2]);
        double driverLatitude = Double.parseDouble(driverLocationArray[0]);
        double driverLongitude = Double.parseDouble(driverLocationArray[1]);
        double driverSpeed = Double.parseDouble(driverLocationArray[2]);

        double distance = calculateDistance(passengerLatitude, passengerLongitude, driverLatitude, driverLongitude);
        double speedDifference = Math.abs(passengerSpeed - driverSpeed);
        return distance < 0.1 && speedDifference < 5;
    }

    // Haversine半正矢公式 来计算地球上两点之间的距离
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}




