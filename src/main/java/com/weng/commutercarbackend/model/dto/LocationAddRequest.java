package com.weng.commutercarbackend.model.dto;

import java.time.LocalDateTime;

public record LocationAddRequest(
        Double longitude,
        Double latitude,
        Double speed
) {
}
