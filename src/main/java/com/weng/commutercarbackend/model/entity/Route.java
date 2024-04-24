package com.weng.commutercarbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 路线表
 * @TableName route
 */
@TableName(value ="route")
@Data
@Builder
public class Route implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 路线号(1~4)
     */
    private Integer number;

    /**
     * 长安校区上车人数
     */
    private Integer changan;

    /**
     * 长安校区东门上下车人数
     */
    private Integer dongmen;

    /**
     * 国际医上下车人数
     */
    private Integer guojiyi;

    /**
     * 紫薇站上下车人数
     */
    private Integer ziwei;

    /**
     * 高新站上下车人数
     */
    private Integer gaoxin;

    /**
     * 劳动南路站上下车人数
     */
    private Integer laodong;

    /**
     * 友谊校区上下车人数
     */
    private Integer youyi;

    /**
     * 云天苑下车人数
     */
    private Integer yun;

    /**
     * 教学西楼下车人数
     */
    private Integer jiaoxi;

    /**
     * 海天苑下车人数
     */
    private Integer hai;

    /**
     * 启翔楼下车人数
     */
    private Integer qixiang;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    /**
     * 
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}