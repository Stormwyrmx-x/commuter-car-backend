package com.weng.commutercarbackend.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weng.commutercarbackend.mapper.PassengerMapper;
import com.weng.commutercarbackend.mapper.StopMapper;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.entity.Stop;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class StopTask {
    @Resource
    private StopMapper stopMapper;
    @Resource
    private PassengerMapper passengerMapper;

    /**
     * 每天0点清空所有的stop表和乘客乘车状态
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void processClear(){
        LambdaUpdateWrapper<Stop> stopLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        stopLambdaUpdateWrapper.set(Stop::getChangan,0);
        stopLambdaUpdateWrapper.set(Stop::getGuojiyi,0);
        stopLambdaUpdateWrapper.set(Stop::getZiwei,0);
        stopLambdaUpdateWrapper.set(Stop::getGaoxin,0);
        stopLambdaUpdateWrapper.set(Stop::getLaodong,0);
        stopLambdaUpdateWrapper.set(Stop::getYouyi,0);
        stopLambdaUpdateWrapper.set(Stop::getUpdateTime, LocalDateTime.now());
        stopMapper.update(stopLambdaUpdateWrapper);

        LambdaUpdateWrapper<Passenger> passengerLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        passengerLambdaUpdateWrapper.set(Passenger::getDriverId,0);
        passengerLambdaUpdateWrapper.set(Passenger::getUpdateTime,LocalDateTime.now());
        passengerMapper.update(passengerLambdaUpdateWrapper);
    }



}
