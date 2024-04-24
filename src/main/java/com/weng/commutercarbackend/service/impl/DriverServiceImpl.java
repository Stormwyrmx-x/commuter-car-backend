package com.weng.commutercarbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.weng.commutercarbackend.common.ResultCodeEnum;
import com.weng.commutercarbackend.exception.BusinessException;
import com.weng.commutercarbackend.mapper.DriverMapper;
import com.weng.commutercarbackend.mapper.RouteMapper;
import com.weng.commutercarbackend.model.dto.LocationAddRequest;
import com.weng.commutercarbackend.model.dto.LoginRequest;
import com.weng.commutercarbackend.model.dto.PasswordChangeRequest;
import com.weng.commutercarbackend.model.dto.RegisterRequest;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.vo.LoginVO;
import com.weng.commutercarbackend.service.DriverService;
import com.weng.commutercarbackend.utils.JwtUtil;
import com.weng.commutercarbackend.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
* @author weng
* @description 针对表【driver(乘客表)】的数据库操作Service实现
* @createDate 2024-03-30 18:30:23
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver>
    implements DriverService {
    
    private final DriverMapper driverMapper;
    private final RouteMapper routeMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final WebSocketServer webSocketServer;
    private final StringRedisTemplate stringRedisTemplate;
    private final Gson gson;
    private final double[][] stops = {
            {34.028930, 108.764328}, // changan
            {34.145423, 108.838777}, // guojiyi
            {34.175846, 108.871443}, // ziwei
            {34.223366, 108.899247}, // gaoxin
            {34.240884,108.910038},  // laodong
            {34.243687, 108.915419}  // youyi
    };
    private final String[] stopNames = {"changan", "guojiyi", "ziwei", "gaoxin", "laodong", "youyi"};

    @Override
    public LoginVO login(LoginRequest loginRequest) {
        //1.根据用户名密码进行登录
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Driver driver = (Driver) authenticationResponse.getPrincipal();
        //2.生成jwt令牌
        String token = jwtUtil.generateToken(driver);
        //3.返回用户信息
        return LoginVO.builder()
                .id(driver.getId())
                .username(driver.getUsername())
                .name(driver.getName())
                .phone(driver.getPhone())
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
        LambdaQueryWrapper<Driver> driverQueryWrapper = new LambdaQueryWrapper<>();
        driverQueryWrapper.eq(Driver::getUsername, registerRequest.username());
        Long count = driverMapper.selectCount(driverQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "账号重复");
        }
        //3.存储到数据库
        Driver driver = Driver.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .name(registerRequest.name())
                .phone(registerRequest.phone())
                .build();
        driverMapper.insert(driver);//如果插入失败，它会抛出异常.而不是返回一个负数

        return driver.getId();
    }

    @Override
    public void checkStop(Long id, LocationAddRequest locationAddRequest) throws IOException {
        for (int i = 0; i < stops.length; i++) {
            double distance = calculateDistance(locationAddRequest.latitude(), locationAddRequest.longitude(),
                    stops[i][0], stops[i][1]);
            if (distance < 1) {
                // If the distance is less than 1km, check if the driver has already arrived at the stop
                //如果已经语音播报过了，则2小时内到达站点不再播报
                HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
                if(hashOperations.get("driver_"+id,stopNames[i])==null){
                    //send a message to the front end via WebSocket
                    Map<String,Object> map=new HashMap<>();
                    map.put("type", 2);//消息类型，2表示语音提醒
                    map.put("message", stopNames[i]);
                    webSocketServer.sendToUser("driver_"+id,gson.toJson(map));

                    //store the stop name in redis
                    hashOperations.put("driver_"+id,stopNames[i],"-");
                    stringRedisTemplate.expire("driver_"+id,2, TimeUnit.HOURS);
                }
                break;
            }
        }
    }

    @Override
    public void changePassword(PasswordChangeRequest passwordChangeRequest) {
        Driver driver = (Driver) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!passwordEncoder.matches(passwordChangeRequest.oldPassword(), driver.getPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "旧密码错误");
        }
        LambdaUpdateWrapper<Driver> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Driver::getId,driver.getId())
                .set(Driver::getPassword,passwordEncoder.encode(passwordChangeRequest.newPassword()));
        driverMapper.update(lambdaUpdateWrapper);
    }

    @Override
    public void updateRouteId(Long routeId) {
        Driver driver = (Driver) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaUpdateWrapper<Driver> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Driver::getId,driver.getId())
                .set(Driver::getRouteId,routeId);
        driverMapper.update(lambdaUpdateWrapper);
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




