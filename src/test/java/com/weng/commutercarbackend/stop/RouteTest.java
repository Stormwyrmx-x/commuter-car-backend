package com.weng.commutercarbackend.stop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.gson.Gson;
import com.weng.commutercarbackend.mapper.DriverMapper;
import com.weng.commutercarbackend.mapper.PassengerMapper;
import com.weng.commutercarbackend.mapper.RouteMapper;
import com.weng.commutercarbackend.model.entity.Driver;
import com.weng.commutercarbackend.model.entity.Passenger;
import com.weng.commutercarbackend.model.entity.Route;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
//@ComponentScan(basePackages = "com.weng.commutercarbackend")
public class RouteTest {

    @Resource
    private RouteMapper routeMapper;
    @Resource
    private DriverMapper driverMapper;
    @Resource
    private PassengerMapper passengerMapper;
    @Resource
    private Gson gson;

//    @Test
//    public void testClearStop() {
//        LambdaQueryWrapper<Driver>driverLambdaQueryWrapper=new LambdaQueryWrapper<>();
//        driverLambdaQueryWrapper.eq(Driver::getId,1);
//        Driver driver = driverMapper.selectOne(driverLambdaQueryWrapper);
//
//        LambdaUpdateWrapper<Stop> stopLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
//        stopLambdaUpdateWrapper.eq(Stop::getId, driver.getStopId());
//        stopLambdaUpdateWrapper.set(Stop::getChangan, 0);
//        stopLambdaUpdateWrapper.set(Stop::getGuojiyi, 0);
//        stopLambdaUpdateWrapper.set(Stop::getZiwei, 0);
//        stopLambdaUpdateWrapper.set(Stop::getGaoxin, 0);
//        stopLambdaUpdateWrapper.set(Stop::getLaodong, 0);
//        stopLambdaUpdateWrapper.set(Stop::getYouyi, 0);
//        stopLambdaUpdateWrapper.set(Stop::getUpdateTime, LocalDateTime.now());
//        stopMapper.update(stopLambdaUpdateWrapper);
//    }

    @Test
    public void testBuildRoute(){
        Route route = Route.builder()
                .changan(1)
                .dongmen(1)
                .build();
        System.out.println("route:"+route);

        System.out.println("RouteBuilder:"+Route.builder()
                .changan(1)
                .dongmen(1)
                .build());
    }


    @Test
    public void testClearStatus(){
        LambdaUpdateWrapper<Passenger> passengerLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        passengerLambdaUpdateWrapper.eq(Passenger::getId, 9);
        passengerLambdaUpdateWrapper.set(Passenger::getDriverId, 0);
        passengerLambdaUpdateWrapper.set(Passenger::getUpdateTime, LocalDateTime.now());
        passengerMapper.update(passengerLambdaUpdateWrapper);
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

    @Test
    public void testSubString(){
        System.out.println(Integer.parseInt("driver_2".substring(7)));
        System.out.println(Integer.parseInt("passenger_9".substring(10)));
    }

}
