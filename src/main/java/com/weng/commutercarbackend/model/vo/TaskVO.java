package com.weng.commutercarbackend.model.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskVO(
        Long id,
        LocalDateTime time,
        Long driverId,
        Long routeId,
        Integer number,
        Long busId,
        String LicensePlate,
        Integer status
) {
}
