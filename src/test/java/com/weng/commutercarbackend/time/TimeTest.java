package com.weng.commutercarbackend.time;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
public class TimeTest {

    @Test
    public void testTime(){
        System.out.println(LocalDateTime.now().getHour() +":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond());
        System.out.println(LocalTime.now());
        System.out.println(LocalTime.now().getMinute());
    }
}
