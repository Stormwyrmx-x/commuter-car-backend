package com.weng.commutercarbackend.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.mapper.StopMapper;
import com.weng.commutercarbackend.model.dto.LocationAddRequest;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.entity.Stop;
import com.weng.commutercarbackend.model.vo.StopVO;
import com.weng.commutercarbackend.service.DriverService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
@Validated
public class DriverController {

    private final DriverService driverService;
    private final StopMapper stopMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取当前登录司机的停靠点
     * @param driver
     * @return
     */
    @GetMapping("/stop")
    public Result<StopVO> getStop(@AuthenticationPrincipal Driver driver) {
        Long stopId = driver.getStopId();
        Stop stop = stopMapper.selectById(stopId);
        StopVO stopVO = StopVO.builder()
                .id(stop.getId())
                .changan(stop.getChangan())
                .guojiyi(stop.getGuojiyi())
                .ziwei(stop.getZiwei())
                .gaoxin(stop.getGaoxin())
                .laodong(stop.getLaodong())
                .youyi(stop.getYouyi())
                .build();
        return Result.success(stopVO);
    }

    /**
     * 每分钟上传司机当前位置，如果靠近站点，则给前端websocket语音播报提醒。
     * @param locationAddRequest
     * @return
     */
    @PostMapping("/location")
    public Result<Boolean> addLocation(@RequestBody @Validated LocationAddRequest locationAddRequest,
                                       @AuthenticationPrincipal Driver driver) throws IOException {
        //存储driver的位置和速度
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("driver_"+driver.getId(), locationAddRequest.time(),
                locationAddRequest.latitude()+","+locationAddRequest.longitude());
        stringRedisTemplate.expire("driver_"+driver.getId(),2, TimeUnit.HOURS);
        //判断是否到站，到站则websocket传给前端语音播报
        driverService.checkStop(driver.getId(),locationAddRequest);
        return Result.success(true);
    }

    /**
     * 修改密码
     * @param password
     * @param driver
     * @return
     */
    @PutMapping("/password")
    public Result<Boolean> changePassword(@NotBlank String password, @AuthenticationPrincipal Driver driver){
        LambdaUpdateWrapper<Driver> driverLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        driverLambdaUpdateWrapper.eq(Driver::getId,driver.getId())
                .set(Driver::getPassword,passwordEncoder.encode(password));
        boolean result = driverService.update(driverLambdaUpdateWrapper);
        return Result.success(result);
    }

}
