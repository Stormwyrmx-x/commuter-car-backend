package com.weng.commutercarbackend.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weng.commutercarbackend.mapper.DriverMapper;
import com.weng.commutercarbackend.mapper.PassengerMapper;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Passenger;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class Clear {

    @Resource
    private DriverMapper driverMapper;
    @Resource
    private PassengerMapper passengerMapper;

    /**
     * 每天0点清空司机和乘客乘车状态
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void processClear(){
        LambdaUpdateWrapper<Driver>driverLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        driverLambdaUpdateWrapper.set(Driver::getRouteId,0);
        driverLambdaUpdateWrapper.set(Driver::getUpdateTime, LocalDateTime.now());
        driverMapper.update(driverLambdaUpdateWrapper);


        LambdaUpdateWrapper<Passenger> passengerLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        passengerLambdaUpdateWrapper.set(Passenger::getDriverId,0);
        passengerLambdaUpdateWrapper.set(Passenger::getRouteId,0);
        passengerLambdaUpdateWrapper.set(Passenger::getGetonStationName,null);
        passengerLambdaUpdateWrapper.set(Passenger::getGetoffStationName,null);
        passengerLambdaUpdateWrapper.set(Passenger::getUpdateTime,LocalDateTime.now());
        passengerMapper.update(passengerLambdaUpdateWrapper);
    }



}
