package com.weng.commutercarbackend.model.dto;

import java.time.LocalDateTime;

public record LocationAddRequest(
        //纬度
        Double latitude,
        //经度
        Double longitude,
        //速度:km/h
        Double speed,
        //时间:格式为HH:mm
        String time
) {
}
