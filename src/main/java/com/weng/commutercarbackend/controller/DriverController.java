package com.weng.commutercarbackend.controller;

import com.weng.commutercarbackend.common.Result;
import com.weng.commutercarbackend.mapper.StopMapper;
import com.weng.commutercarbackend.model.dto.LocationAddRequest;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Stop;
import com.weng.commutercarbackend.model.vo.StopVO;
import com.weng.commutercarbackend.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final StopMapper stopMapper;
    private final StringRedisTemplate stringRedisTemplate;

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
                .imcXa(stop.getImcXa())
                .ziwei(stop.getZiwei())
                .gaoxin(stop.getGaoxin())
                .laodong(stop.getLaodong())
                .youyi(stop.getYouyi())
                .build();
        return Result.success(stopVO);
    }

    /**
     * 每分钟上传司机当前位置，如果靠近站点，则给前端websocket语音播报提醒。
     * 如果到达终点站，则清空对应的stop表
     * @param locationAddRequest
     * @return
     */
    @PostMapping("/location")
    public Result<Boolean> addLocation(@RequestBody @Validated LocationAddRequest locationAddRequest,
                                       @AuthenticationPrincipal Driver driver) {
        //存储driver的位置和速度
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("driver"+driver.getId(),
                LocalDateTime.now().getHour() +":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond(),
                locationAddRequest.longitude()+","+locationAddRequest.latitude()+","+locationAddRequest.speed());
        //判断是否到站



        return Result.success(true);
    }


}
