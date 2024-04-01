package com.weng.commutercarbackend.controller;


import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.model.dto.LocationAddRequest;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.vo.PassengerVO;
import com.weng.commutercarbackend.model.vo.StopVO;
import com.weng.commutercarbackend.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 获取当前登录乘客信息
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
                locationAddRequest.latitude()+","+locationAddRequest.longitude()+","+locationAddRequest.speed());
        stringRedisTemplate.expire("passenger_"+passenger.getId(),2, TimeUnit.HOURS);
        //判断是否已经上传有10次且乘客的状态为0，如果有则拿乘客的数据和司机的数据进行比对
        //如果匹配成功则向前端传递数据，如果不成功则不传
        if (hashOperations.keys("passenger_"+passenger.getId()).size() >= 10 && passenger.getDriverId()==0){
            passengerService.compareLocation(passenger.getId());
        }
        return Result.success(true);
    }

}
