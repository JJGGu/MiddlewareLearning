package com.study.middleware.server.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RedisConfigTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        log.info("Start");
        String key = "redis:template:one:string";
        String value = "sssss";
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);

        Object o = valueOperations.get(key);
        log.info(o.toString());
    }
}