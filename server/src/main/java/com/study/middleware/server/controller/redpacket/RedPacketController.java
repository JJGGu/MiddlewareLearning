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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
}
