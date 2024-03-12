package com.weng.commutercarbackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @TableName user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String role;

    private Integer status;

    private BigDecimal money;

    private String token;

}