package com.weng.commutercarbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class CommuterCarBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuterCarBackendApplication.class, args);
    }

}
