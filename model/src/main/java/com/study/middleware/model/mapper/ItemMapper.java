package com.study.middleware.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.middleware.model.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

/**
 * @program: middleware
 * @description:
 * @author: JJGGu
 * @create: 2020-10-19 10:30
 **/
@Mapper
public interface ItemMapper extends BaseMapper<Item> {

}
