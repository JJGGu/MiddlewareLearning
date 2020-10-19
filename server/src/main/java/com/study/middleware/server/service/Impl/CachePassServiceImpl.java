package com.study.middleware.server.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.middleware.model.entity.Item;
import com.study.middleware.model.mapper.ItemMapper;
import com.study.middleware.server.service.CachePassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @program: middleware
 * @description:
 * @author: JJGGu
 * @create: 2020-10-19 10:34
 **/
@Slf4j
@Service
public class CachePassServiceImpl extends ServiceImpl<ItemMapper, Item> implements CachePassService{

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;


    private static final String prefix = "item:";
    // 缓存穿透实战
    public Item getItemByCode(String code) throws Exception{
        String key = prefix + code;
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 先判断缓存中有没有，有的话直接反序列化
        if (redisTemplate.hasKey(key)) {
            log.info("Get From redis");
            String s = valueOperations.get(key);
            if (!s.equals("")) {
                Item item = objectMapper.readValue(s, Item.class);
                return item;
            } else {
                return null;
            }
        } else {
            // 查询数据库如果数据库中有
            Item item = itemMapper.selectOne(new QueryWrapper<Item>().eq("code", code));
            if (item !=null) {
                // 如果有的话将对象序列化后写入缓存
                log.info("Get from mysql");
                String value = objectMapper.writeValueAsString(item);
                valueOperations.set(key, value);
                return item;
            } else {
                // 如果没有的话将该键对应的空值写入缓存
                log.info("数据库中也没有该数据");
                valueOperations.set(key, "", 30, TimeUnit.SECONDS);
            }
        }
        return null;
    }
}
