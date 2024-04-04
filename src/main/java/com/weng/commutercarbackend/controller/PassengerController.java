package com.weng.commutercarbackend.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.common.ResultCodeEnum;
import com.weng.commutercarbackend.model.dto.LocationAddRequest;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.vo.PassengerVO;
import com.weng.commutercarbackend.model.vo.StopVO;
import com.weng.commutercarbackend.service.DriverService;
import com.weng.commutercarbackend.service.PassengerService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
@Validated
public class PassengerController {

    private final PassengerService passengerService;
    private final DriverService driverService;
    private final StringRedisTemplate stringRedisTemplate;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取当前登录乘客信息，包括了默认下车站点，当前余额，当前司机id
     * @param passenger
     * @return
     */
    @GetMapping("/current")
    public Result<PassengerVO> getCurrentPassenger(@AuthenticationPrincipal Passenger passenger) {
        PassengerVO passengerVO = PassengerVO.builder()
                .id(passenger.getId())
                .username(passenger.getUsername())
                .name(passenger.getName())
                .phone(passenger.getPhone())
                .stationName(passenger.getStationName())
                .driverId(passenger.getDriverId())
                .money(passenger.getMoney())
                .build();
        return Result.success(passengerVO);
    }


    /**
     * 乘客选择/修改下车地点
     * @param stationName
     * @param passenger
     * @return
     */
    @PutMapping("/stationName")
    public Result<Boolean>updateStationName(@NotBlank String stationName, @AuthenticationPrincipal Passenger passenger) throws IOException {
        boolean result;
        //如果乘客还未监测在车上，就不能选择下车地点
        if (passenger.getDriverId()==0){
            Result.error(ResultCodeEnum.FORBIDDEN_ERROR,"系统尚未监测您在车上，请耐心等待");
        }else {
            passengerService.updateStationName(passenger,stationName);
        }
        return Result.success(true);
    }


    /**
     * 每分钟上传乘客当前位置
     * @param locationAddRequest
     * @return
     */
    @PostMapping("/location")
    public Result<Boolean> addLocation(@RequestBody @Validated LocationAddRequest locationAddRequest,
                                       @AuthenticationPrincipal Passenger passenger) throws IOException {
        //存储passenger的位置和速度
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("passenger_"+passenger.getId(),locationAddRequest.time(),
                locationAddRequest.latitude()+","+locationAddRequest.longitude());
        //应该先存储数据，然后再设置过期时间
        stringRedisTemplate.expire("passenger_"+passenger.getId(),2, TimeUnit.HOURS);
        //判断是否已经上传有10次且乘客的状态为0，如果有则拿乘客的数据和司机的数据进行比对
        //如果匹配成功则向前端传递数据，如果不成功则不传
        if (hashOperations.keys("passenger_"+passenger.getId()).size() >= 10 && passenger.getDriverId()==0){
            passengerService.compareLocation(passenger.getId());
        }
        return Result.success(true);
    }

    /**
     * 修改余额
     * @param money
     * @param passenger
     * @return
     */
    @PutMapping("/payment")
    public Result<BigDecimal> payment(@NotNull @Min(0) BigDecimal money, @AuthenticationPrincipal Passenger passenger){
        //乘客支付
        passengerService.payment(money,passenger);
        return Result.success(passenger.getMoney().add(money));
    }

    /**
     * 修改密码
     * @param password
     * @param passenger
     * @return
     */
    @PutMapping("/password")
    public Result<Boolean> changePassword(@NotBlank String password, @AuthenticationPrincipal Passenger passenger){
        LambdaUpdateWrapper<Passenger> passengerLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        passengerLambdaUpdateWrapper.eq(Passenger::getId,passenger.getId())
                .set(Passenger::getPassword,passwordEncoder.encode(password));
        boolean result = passengerService.update(passengerLambdaUpdateWrapper);
        return Result.success(result);
    }

}
