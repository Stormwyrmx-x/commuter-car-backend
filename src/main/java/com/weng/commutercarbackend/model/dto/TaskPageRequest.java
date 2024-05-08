package com.weng.commutercarbackend.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TaskPageRequest(
        @NotNull(message = "当前页不能为空")
        @Min(value = 1,message = "当前页不能小于1")
        Integer current,

        @NotNull(message = "每页条数不能为空")
        @Min(value = 1,message = "每页条数不能小于1")
        Integer size,

        @Min(value = 1, message = "司机号不能小于1")
        Long driverId,

        @Min(value = 0, message = "状态不能小于0")
        @Max(value = 2, message = "状态不能大于2")
        Integer status,

        @Min(value = 1, message = "路线号不能小于1")
        @Max(value = 4, message = "路线号不能大于4")
        Integer number
) {
}
