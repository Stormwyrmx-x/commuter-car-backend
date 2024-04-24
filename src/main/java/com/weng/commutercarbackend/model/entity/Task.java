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
 * 工单表
 * @TableName task
 */
@TableName(value ="task")
@Data
@Builder
public class Task implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发车时间
     */
    private LocalDateTime time;

    /**
     * 司机id
     */
    private Long driverId;

    /**
     * 路线id
     */
    private Long routeId;

    /**
     * 车辆id
     */
    private Long busId;

    /**
     * 工单状态（0-未分配，1-已分配，2-已执行）
     */
    private Integer status;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;

    /**
     * 0-正常，1-被删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}