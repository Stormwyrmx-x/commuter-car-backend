package com.weng.commutercarbackend.model.vo;

import lombok.Builder;

@Builder
public record BusVO(
        Long id,
        String licensePlate
) {
}
