package com.study.middleware.server.service.Impl;

import com.study.middleware.model.entity.RedDetail;
import com.study.middleware.model.entity.RedPacketDto;
import com.study.middleware.model.entity.RedRecord;
import com.study.middleware.server.common.R;
import com.study.middleware.server.service.RedDetailService;
import com.study.middleware.server.service.RedPacketService;
import com.study.middleware.server.service.RedRecordService;
import com.study.middleware.server.service.RedRobRecordService;
import com.study.middleware.server.utils.RedPacketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    }
}
