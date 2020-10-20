package com.study.middleware.server.service.Impl;

import com.study.middleware.model.entity.RedDetail;
import com.study.middleware.model.entity.RedPacketDto;
import com.study.middleware.model.entity.RedRecord;
import com.study.middleware.model.entity.RedRobRecord;
import com.study.middleware.server.common.R;
import com.study.middleware.server.service.RedDetailService;
import com.study.middleware.server.service.RedPacketService;
import com.study.middleware.server.service.RedRecordService;
import com.study.middleware.server.service.RedRobRecordService;
import com.study.middleware.server.utils.RedPacketUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: middleware
 * @description:
 * @author: JJGGu
 * @create: 2020-10-20 08:36
 **/
@Slf4j
@EnableAsync
@Service
public class RedPacketServiceImpl implements RedPacketService {
    @Resource
    private RedRecordService redRecordService;
    @Resource
    private RedRobRecordService redRobRecordService;
    @Resource
    private RedDetailService redDetailService;
    @Resource
    private RedisTemplate redisTemplate;

    private static final String keyPrefix = "redis:red:packet";
    @Override
    public String handOut(RedPacketDto redPacketDto) throws Exception {
        Integer amount = redPacketDto.getAmount();
        Integer total = redPacketDto.getTotal();
        if (amount > 0 && total > 0) {
            // 生成红包
            List<Integer> list = RedPacketUtils.generateRedPacket(amount, total);
            // 纳米级时间生成红包唯一ID
            String tempStamp = Long.toString(System.nanoTime());
            // 将list放入缓存中，缓存list的key
            String redPacketId = keyPrefix + ":" + redPacketDto.getUserId() + ":" + tempStamp;
            redisTemplate.opsForList().leftPushAll(redPacketId, list);
            // 将total也放入缓存中
            String totalKey = redPacketId + ":total";
            log.info("redPacketId: {}, totalKey: {}", redPacketId, totalKey);
            redisTemplate.opsForValue().set(totalKey, total);
            // 将红包个数与随机金额也写入数据库
            recordRedPacket(redPacketDto, redPacketId, list);
            return redPacketId;
        } else {
            throw new Exception("数据异常");
        }
    }

    @Override
    public BigDecimal rob(Integer userId, String redPacketId) throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 处理用户抢红包之前，判断当前用户是否已经抢过红包
        // 如果已经抢过，直接返回金额给前端
        Object o = valueOperations.get(redPacketId + ":" + userId + ":rob");
        if (o != null) {
            log.info("该用户{}已经抢过了，直接显示出来", userId);
            return new BigDecimal(o.toString());
        }
        // 然后判断当前是否还有红包
        Boolean click = click(redPacketId);
        String lockKey = redPacketId + userId +":lock";
        if (click) {
            // 由分布式锁判断该用户抢过了没有
            Boolean absent = redisTemplate.opsForValue().setIfAbsent(lockKey, redPacketId, 24, TimeUnit.HOURS);

            // 说明还有红包，那就开始拆红包
            if (absent) {
                Object value = redisTemplate.opsForList().rightPop(redPacketId);
                if (value != null) {
                    // 成功抢到了红包，更新缓存中的total
                    String totalKey = redPacketId + ":total";
                    Integer curValue = valueOperations.get(totalKey) == null ? 0 : (Integer)valueOperations.get(totalKey);
                    valueOperations.set(totalKey, curValue - 1);

                    // 以元为单位返回给前端
                    BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
                    // 将记录写入数据库
                    recordRobRedPacket(userId, redPacketId, result);
                    // 将该条记录写入缓存
                    valueOperations.set(redPacketId + ":" + userId + ":rob", result, 24, TimeUnit.HOURS);
                    log.info("抢到红包啦红包：absent: {}, userId: {}, amount: {}",absent, userId, result);
                    return result;
                }
            }

        }
        return null;
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recordRedPacket(RedPacketDto redPacketDto, String redPacketId, List<Integer> list) throws Exception {
        RedRecord redRecord = new RedRecord();
        redRecord.setRedPacketId(redPacketId);
        redRecord.setAmount(BigDecimal.valueOf(redPacketDto.getAmount()));
        redRecord.setCreateTime(new Date());
        redRecord.setTotal(redPacketDto.getTotal());
        redRecord.setUserId(redPacketDto.getUserId());
        boolean save = redRecordService.save(redRecord);

        // 将详情信息存储
        RedDetail redDetail;
        for (Integer i : list) {
            redDetail = new RedDetail();
            redDetail.setRedRecordId(redRecord.getId());
            redDetail.setAmount(BigDecimal.valueOf(i));
            redDetail.setCreateTime(new Date());
            redDetailService.save(redDetail);
        }
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void recordRobRedPacket(Integer userId, String redPacketId, BigDecimal amount) throws Exception {
        RedRobRecord redRobRecord = new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacketId(redPacketId);
        redRobRecord.setAmount(amount);
        redRobRecord.setRobTime(new Date());

        redRobRecordService.save(redRobRecord);
    }

    // 点红包判断是否还有未领取的
    private Boolean click(String redPacketId) {
        log.info("click redpacketId : {}", redPacketId);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object total = valueOperations.get(redPacketId + ":total");
        if (total != null && Integer.parseInt(total.toString()) > 0) {
            return true;
        }
        return false;
    }
}
