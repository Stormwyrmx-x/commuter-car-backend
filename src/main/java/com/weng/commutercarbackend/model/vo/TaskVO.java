package com.weng.commutercarbackend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
public record TaskVO(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime time,
        Long driverId,
        //司机工号
        String username,
        //司机姓名
        String name,
        Long routeId,
        Integer number,
        Long busId,
        String licensePlate,
        Integer status
) {
}
