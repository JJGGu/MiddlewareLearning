package com.study.middleware.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class RedPacketUtilsTest {

    @Test
    void test() {
        List<Integer> list = RedPacketUtils.generateRedPacket(100, 3);
        log.info("list: {}", list);
    }

}