package com.weng.commutercarbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 站点表
 * @TableName stop
 */
@TableName(value ="stop")
@Data
public class Stop implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 长安校区下车人数
     */
    private Integer changan;

    /**
     * 国际医下车人数
     */
    private Integer imcXa;

    /**
     * 紫薇站下车人数
     */
    private Integer ziwei;

    /**
     * 高新站下车人数
     */
    private Integer gaoxin;

    /**
     * 劳动南路站下车人数
     */
    private Integer laodong;

    /**
     * 友谊校区下车人数
     */
    private Integer youyi;

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