package com.weng.commutercarbackend.model.vo;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DriverVO(
         Long id,

        /**
         * 学号/工号
         */
         String username,

        /**
         * 用户姓名
         */
         String name,

        /**
         * 电话号
         */
         String phone,

        /**
         * 站点表id
         */
         Long stopId
) {
}
