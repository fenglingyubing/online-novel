package com.fengling.common.constant;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    /**
     * 操作成功后的返回
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败后的返回
     */
    FAIL(500, "操作失败"),

    /**
     * 参数无效
     */
    PARAM_NOT_VALID(501, "参数无效"),

    /**
     * 用户未登录或登录已失效的返回
     */
    UNAUTHORIZED(401, "未登录或登录已失效"),

    /**
     * 无权限访问
     */
    FORBIDDEN(403, "无权限访问"),

    /**
     * 资源未找到
     */
    NOT_FOUND(404, "资源未找到"),

    /**
     * 用户名已存在的返回
     */
    USERNAME_EXIST(1001, "用户名已存在"),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误"),

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(1003, "用户不存在"),

    /**
     * 用户已被禁用
     */
    ACCOUNT_DISABLED(1004, "用户已被禁用");

    private ResultCodeEnum(Integer code, String message) {
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
