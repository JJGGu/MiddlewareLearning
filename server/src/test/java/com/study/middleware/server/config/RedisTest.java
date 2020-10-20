package com.study.middleware.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.middleware.model.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    // jackson 序列化
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    // 对象序列化
    @Test
    public void test2() throws Exception {
        Book book = new Book(1, "studyEnglish");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = "study:book1";
        String value = objectMapper.writeValueAsString(book);
        log.info("值为： {}", value);
        valueOperations.set(key, value);

        Object o = valueOperations.get(key);
        if (o != null) {
            Book book1 = objectMapper.readValue(o.toString(), Book.class);
            log.info("book: {}", book1.toString());
        } else {
            log.info("未获取到该键的值");

        }

    }

    // 测试StringRedisTemplate
    @Test
    public void test3() {
        String key = "redis:stringtemplate:one";
        String value = "a string aaaa";

        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set(key, value);

        log.info("result: {}", stringValueOperations.get(key));
    }

    // 测试List存储
    @Test
    public void test4() {
        List<Book> list = new ArrayList<>();
        list.add(new Book(1, "三国演义"));
        list.add(new Book(2, "红楼梦"));

        String key = "redis:list:test1";
        ListOperations listOperations = redisTemplate.opsForList();
        // 底层是双向循环链表，需要一个一个存进去然后一个个取出来
        for (Book book: list) {
            listOperations.leftPush(key, book);
        }

        Object object = listOperations.rightPop(key);
        Book temp;
        while (object != null) {
            temp = (Book) object;
            log.info("book: {}", temp.toString());
            object = listOperations.rightPop(key);
        }
    }

    @Test
    public void testRedisSet() {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = "redis:set:test";
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("aaa");

        for (String s: list) {
            setOperations.add(key, s);
        }
        String value = setOperations.pop(key);
        while (value != null) {
            log.info("value: {}", value);
            value = setOperations.pop(key);
        }
    }

    // 测试hash
    @Test
    public void testRedisHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1, "ddd"));
        bookList.add(new Book(2, "ccc"));
        bookList.add(new Book(3, "eee"));
        String key = "redis:hash:test";
        for (Book book: bookList) {
            hashOperations.put(key, book.getBookNo() + "", book);

        }
        // 获取所有的对象
        Map<Integer, Book> entries = hashOperations.entries(key);
        log.info("map: {}", entries);

        Object o = hashOperations.get(key, 1 + "");
        log.info("object: {}",o);
    }

    // 测试失效
    @Test
    public void testExpire() throws Exception{
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = "redis:test:expire";
        valueOperations.set(key, "失效测试", 6L, TimeUnit.SECONDS);
        Object o = valueOperations.get(key);
        log.info("o: {}", o);
        Thread.sleep(5000);
        Object o1 = valueOperations.get(key);

        log.info("o1: {}, hasKey: {}", o1, redisTemplate.hasKey(key));
    }
}