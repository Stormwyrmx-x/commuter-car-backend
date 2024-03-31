package com.weng.commutercarbackend.model.vo;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PassengerVO(
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
         * 默认下车站点名称
         */
         String stationName,

        /**
         * 0-不在车上，1+（显示的对应司机的id，表示在哪个司机的车上）
         */
         Long driverId,

        /**
         * 钱包余额
         */
         BigDecimal money
) {
}
