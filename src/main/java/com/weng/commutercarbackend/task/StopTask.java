package com.weng.commutercarbackend.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.weng.commutercarbackend.mapper.StopMapper;
import com.weng.commutercarbackend.model.entity.Stop;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StopTask {
    @Resource
    private StopMapper stopMapper;

    /**
     * todo
     * 每天0点清空stop表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void processClearStop(){

        LambdaUpdateWrapper<Stop> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.clear();
        lambdaUpdateWrapper.set(Stop::getChangan,0);
        lambdaUpdateWrapper.set(Stop::getImcXa,0);
        lambdaUpdateWrapper.set(Stop::getZiwei,0);
        lambdaUpdateWrapper.set(Stop::getGaoxin,0);
        lambdaUpdateWrapper.set(Stop::getLaodong,0);
        lambdaUpdateWrapper.set(Stop::getYouyi,0);
        stopMapper.update(lambdaUpdateWrapper);
    }

}
