package com.study.middleware.server.controller.redis;

import com.study.middleware.model.entity.Item;
import com.study.middleware.server.common.R;
import com.study.middleware.server.service.CachePassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: middleware
 * @description: 缓存穿透controller
 * @author: JJGGu
 * @create: 2020-10-19 09:34
 **/
@Slf4j
@RestController
@RequestMapping("cache")
public class CachePassController {

    @Resource
    private CachePassService cachePassService;
    @GetMapping("code")
    public R<Item> getItem(String code) throws Exception{
        log.info("getItem");
        Item itemByCode = cachePassService.getItemByCode(code);
        if (itemByCode == null) {
            return new R<>(1000, "没有查到");
        }
        return new R<>(itemByCode);
    }
}
