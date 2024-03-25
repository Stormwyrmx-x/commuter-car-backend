package com.weng.commutercarbackend.redis;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor
public class RedisTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private Integer count=0;

    @Test
    public void testRedis() {
        //存储driver和passenger的位置和速度
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("driver", "location", "101.123123,123.123123");
        hashOperations.put("driver", "speed", "46");

        hashOperations.put("passenger", "location", "101.123124,123.123121");
        hashOperations.put("passenger", "speed", "47");

        //拿出位置和速度
        String driverLocation = hashOperations.get("driver", "location");
        String driverSpeed = hashOperations.get("driver", "speed");
        String passengerLocation = hashOperations.get("passenger", "location");
        String passengerSpeed = hashOperations.get("passenger", "speed");

        String[] driverLocationParts = driverLocation.split(",");
        String[] passengerLocationParts = passengerLocation.split(",");

        double driverLatitude = Double.parseDouble(driverLocationParts[0]);
        double driverLongitude = Double.parseDouble(driverLocationParts[1]);
        double passengerLatitude = Double.parseDouble(passengerLocationParts[0]);
        double passengerLongitude = Double.parseDouble(passengerLocationParts[1]);

        int driverSpeedInt = Integer.parseInt(driverSpeed);
        int passengerSpeedInt = Integer.parseInt(passengerSpeed);

        //计算位置和速度的差值
        double latitudeDifference = Math.abs(driverLatitude - passengerLatitude);
        double longitudeDifference = Math.abs(driverLongitude - passengerLongitude);
        int speedDifference = Math.abs(driverSpeedInt - passengerSpeedInt);

        //判断位置和速度的差值是否在合理范围内，并记录到redis中
        if(latitudeDifference < 0.00001 && longitudeDifference < 0.00001 && speedDifference < 3){
            stringRedisTemplate.opsForValue().set("isMatch_"+(++count),"true");
        }else {
            stringRedisTemplate.opsForValue().set("isMatch_"+(++count),"false");
        }
        //如果超过三次，则开始判断人是否在车上
        if (count>=3){
            Set<String> keys = stringRedisTemplate.keys("isMatch_*");
            if (keys != null) {
                keys.forEach(key->{
                    if (!Objects.equals(stringRedisTemplate.opsForValue().get(key), "true")){
                        System.out.println("false");
                    }
                });
            }
        }
    }
}
