package com.study.middleware.server.service;

import com.study.middleware.model.entity.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

public interface RedPacketService {
    String handOut(RedPacketDto redPacketDto) throws Exception;
    BigDecimal rob(Integer userId, String redPacketId) throws Exception;
    // 将红包的唯一id、总数、红包金额记录到数据库
    void recordRedPacket(RedPacketDto redPacketDto, String redPacketId, List<Integer> list) throws Exception;
    // 记录用户抢红包到数据库
    void recordRobRedPacket(Integer userId, String redPacketId, BigDecimal amount) throws Exception;
}
