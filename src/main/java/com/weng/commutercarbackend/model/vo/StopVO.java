package com.weng.commutercarbackend.model.vo;

import lombok.Builder;

@Builder
public record StopVO(

         Long id,

        /**
         * 长安校区下车人数
         */
         Integer changan,

        /**
         * 国际医下车人数
         */
         Integer guojiyi,

        /**
         * 紫薇站下车人数
         */
         Integer ziwei,

        /**
         * 高新站下车人数
         */
         Integer gaoxin,

        /**
         * 劳动南路站下车人数
         */
         Integer laodong,

        /**
         * 友谊校区下车人数
         */
         Integer youyi
) {
}
