package com.study.middleware.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: middleware
 * @description: 红包记录
 * @author: JJGGu
 * @create: 2020-10-19 18:33
 **/
@Data
public class RedRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String redPacketId;
    private Integer total;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;
}
