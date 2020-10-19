package com.study.middleware.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.middleware.model.entity.Item;

public interface CachePassService extends IService<Item> {
    Item getItemByCode(String code) throws Exception;
}
