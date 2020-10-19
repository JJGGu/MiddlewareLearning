package com.study.middleware.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: middleware
 * @description: 红包详情
 * @author: JJGGu
 * @create: 2020-10-19 18:41
 **/
@Data
public class RedDetail {
    private Integer id;
    private Integer redRecordId;
    private BigDecimal amount;
    private Byte isActive;
    private Date createTime;
}
