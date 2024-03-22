package com.fzq.shopservice.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * Shop table
 * @TableName Shops
 */
@TableName(value ="Shops")
@Data
public class Shops implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * shop name
     */
    private String shopName;

    /**
     * shop avatar url
     */
    private String avatarUrl;

    /**
     * shop description
     */
    private String description;

    /**
     * shop level
     */
    private Integer shopLevel;

    /**
     * shop score, range 0.00 - 5.00
     */
    private BigDecimal shopScore;

    /**
     * shop status, 0 - approved, 1 - pending, 2 - rejected
     */
    private Integer shopStatus;

    /**
     * create time
     */
    private Date createTime;

    /**
     * update time
     */
    private Date updateTime;

    /**
     * is delete?
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}