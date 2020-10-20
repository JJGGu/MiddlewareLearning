package com.study.middleware.server.common;

/**
 * @program: middleware
 * @description: 状态枚举类
 * @author: JJGGu
 * @create: 2020-10-19 22:24
 **/
public enum StatusCode {
    Success(0, "成功"),
    Fail(-1, "失败"),
    InvalidParams(201, "非法参数"),
    InvalidGrantType(202, "非法授权类型");

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
