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
          * 上车站点名称
          */
         String getonStationName,

         /**
          * 下车站点名称
          */
         String getoffStationName,

        /**
         * 0-不在车上，1+（显示的对应司机的id，表示在哪个司机的车上）
         */
         Long driverId,

         /**
          * 路线id
          */
         Long routeId,

         /**
         * 路线号(1~4)
         */
         Integer number,


         /**
          * 钱包余额
          */
         BigDecimal money
) {
}
