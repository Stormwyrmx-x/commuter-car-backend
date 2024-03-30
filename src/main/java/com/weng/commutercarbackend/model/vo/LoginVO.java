package com.weng.commutercarbackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @TableName user
 */
@Builder
public record LoginVO(
        Long id,
        String username,
        String name,
        String phone,
        String token
) {
}