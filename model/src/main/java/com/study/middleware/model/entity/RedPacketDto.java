package com.study.middleware.model.entity;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: middleware
 * @description: 接受红包个数与总额参数
 * @author: JJGGu
 * @create: 2020-10-19 21:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketDto {
    private Integer userId;
    @NotNull
    private Integer total;
    // 单位为分
    @NotNull
    private Integer amount;
}
