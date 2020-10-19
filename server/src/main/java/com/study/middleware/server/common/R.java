package com.study.middleware.server.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: middleware
 * @description: 返回模型
 * @author: JJGGu
 * @create: 2020-10-19 11:32
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class R<T> {
    private Boolean success = true;
    private T data;
    private Integer errorCode;
    private String errorMsg;

    // 正常返回
    public R(T data) {
        this.data = data;
    }

    // 错误返回
    public R(Integer code, String msg) {
        success = false;
        errorCode = code;
        errorMsg = msg;
    }
}
