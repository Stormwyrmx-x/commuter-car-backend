package com.weng.commutercarbackend.stop;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.gson.Gson;
import com.weng.commutercarbackend.mapper.StopMapper;
import com.weng.commutercarbackend.model.entity.Stop;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
//@ComponentScan(basePackages = "com.weng.commutercarbackend")
public class StopTest {

    @Resource
    private StopMapper stopMapper;

    @Resource
    private Gson gson;

    @Test
    public void testClearStop() {
        LambdaUpdateWrapper<Stop> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Stop::getChangan,0);
        lambdaUpdateWrapper.set(Stop::getGuojiyi,0);
        lambdaUpdateWrapper.set(Stop::getZiwei,0);
        lambdaUpdateWrapper.set(Stop::getGaoxin,0);
        lambdaUpdateWrapper.set(Stop::getLaodong,0);
        lambdaUpdateWrapper.set(Stop::getYouyi,0);
        stopMapper.update(lambdaUpdateWrapper);

    }

    @Test
    public void testGson(){
        Map<String,Object> map=new HashMap<>();
        map.put("type", 2);//消息类型，2表示语音提醒
        map.put("message", "前方到站：" + "ziwei");
        System.out.println(gson.toJson(map));
    }

    @Test
    // Haversine半正矢公式 来计算地球上两点之间的距离
    public void calculateDistance() {
        double lat1=34.240884;
        double lon1=108.910038;
        double lat2=34.243687;
        double lon2=108.915419;
        int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        System.out.println(R * c);
    }

}
