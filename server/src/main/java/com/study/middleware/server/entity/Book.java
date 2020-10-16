package com.study.middleware.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: middleware
 * @description: 实体类
 * @author: JJGGu
 * @create: 2020-10-16 09:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book implements Serializable {
    private Integer bookNo;
    private String name;
}
