package com.fengling.common.constant;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    /**
     * 操作成功后的返回
     */
    SUCCESS(200,"操作成功"),
    /**
     * 操作失败后的返回
     */
    FAIL(500,"操作失败"),
    /**
     * 用户未登录或登录已失效的返回
     */
    UNAUTHORIZED(401, "未登录或登录已失效"),
    /**
     * 用户名已存在的返回
     */
    USERNAME_EXIST(1001,"用户名已存在");

    private ResultCodeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    /**
     * 响应码
     */
    private final Integer code;
    /**
     * 响应消息
     */
    private final String message;
}
