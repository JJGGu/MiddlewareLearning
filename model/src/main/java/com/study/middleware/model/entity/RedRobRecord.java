package com.study.middleware.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: middleware
 * @description: 抢红包记录
 * @author: JJGGu
 * @create: 2020-10-19 18:43
 **/
@Data
public class RedRobRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String redPacketId;
    private BigDecimal amount;
    private Date robTime;
    private Byte isActive;
}
