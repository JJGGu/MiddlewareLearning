package com.study.middleware.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @program: middleware
 * @description: 根据总额了数量随机生成红包
 * @author: JJGGu
 * @create: 2020-10-19 19:46
 **/
public class RedPacketUtils {
    // 生成红包，输入总金额都是以分为单位，所以为Integer
    public static List<Integer> generateRedPacket(Integer amount, Integer total) {
        List<Integer> list = new ArrayList<>();
        // 检查输入合法性
        if (amount > 0 || total > 0) {
            Random random = new Random();
            while(total - 1 > 0) {
                // 使用二倍均值法选取边界
                int borden = amount / total * 2;
                // 随机生成金额，需要保证大于1
                int randomAmount = random.nextInt(borden - 1) + 1;
                // 剩余的金额
                amount -= randomAmount;
                // 剩余的红包数量
                total--;
                list.add(randomAmount);
            }
            list.add(amount);
        }
        return list;

    }
}
