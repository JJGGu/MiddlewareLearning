package com.study.middleware.server.controller.redpacket;

import com.study.middleware.model.entity.RedPacketDto;
import com.study.middleware.model.entity.RedRobRecord;
import com.study.middleware.server.common.R;
import com.study.middleware.server.common.StatusCode;
import com.study.middleware.server.service.RedDetailService;
import com.study.middleware.server.service.RedPacketService;
import com.study.middleware.server.service.RedRecordService;
import com.study.middleware.server.service.RedRobRecordService;
import com.study.middleware.server.utils.RedPacketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: middleware
 * @description: 抢红包控制器
 * @author: JJGGu
 * @create: 2020-10-19 22:05
 **/
@Slf4j
@RestController
@RequestMapping("redPacket")
public class RedPacketController {

    @Resource
    private RedPacketService redPacketService;
    /*
    * 发红包POST请求
    * 请求参数用JSON格式提交
    * BindingResult: 用于参数校验返回结果
    * */
    @PostMapping(value = "handOut", consumes = MediaType.APPLICATION_JSON_VALUE)
    public R handOut(@Validated @RequestBody RedPacketDto redPacketDto, BindingResult result) {
        log.info("radpacketDto: {}", redPacketDto);
        // 参数校验
        if (result.hasErrors()) {
            return new R(StatusCode.InvalidParams);
        }
        try{
            String redPacketId = redPacketService.handOut(redPacketDto);
            // 返回红包唯一标识给前端
            return new R(redPacketId);
        } catch (Exception e) {
            log.error("发红包异常 redPacketDto : {}", redPacketDto, e.fillInStackTrace());
            return new R(StatusCode.Fail);
        }
    }

    @GetMapping("rob")
    public R rob(Integer userId, String redPacketId) {
        try{
            log.info("接受参数为：userId:{}, redPacketId: {}", userId, redPacketId);
            BigDecimal result = redPacketService.rob(userId, redPacketId);
            if (result != null) {
                // 说明抢到了红包，返回给前端,这里为了前端方便，直接将单位转换为元
                return new R(result);
            } else {
                return new R(StatusCode.Fail.getCode(), "红包已被抢完");
            }
        } catch (Exception e) {
            // 如果发生异常
            log.info("抢红包发生异常：userId:{}, redPacketId: {}", userId, redPacketId, e.fillInStackTrace());
            return new R(StatusCode.Fail.getCode(), e.getMessage());
        }
    }
}
