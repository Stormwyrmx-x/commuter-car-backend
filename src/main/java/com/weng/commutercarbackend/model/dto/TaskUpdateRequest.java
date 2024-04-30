package com.weng.commutercarbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskUpdateRequest(

        @NotNull
        @Min(value = 1, message = "id不能小于1")
        Long id,

        @NotNull
        //这里要使用@JsonFormat注解，不能用@DateTimeFormat
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime time,

        @Min(value = 1, message = "司机号不能小于1")
        Long driverId,

        @NotNull
        @Min(value = 1, message = "路线号不能小于1")
        Long routeId,

        @NotNull
        @Min(value = 1, message = "路线号不能小于1")
        @Max(value = 4, message = "路线号不能大于4")
        Integer number,

        @NotNull
        @Min(value = 1,message = "车辆id不能小于1")
        Long busId,

        @NotNull
        @Min(value = 0, message = "状态不能小于0")
        @Max(value = 2, message = "状态不能大于2")
        Integer status
) {
}
