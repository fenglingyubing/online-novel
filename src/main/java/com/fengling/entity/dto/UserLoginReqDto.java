package com.fengling.entity.dto;

import lombok.Data;

/**
 * 用户登录请求参数
 */
@Data
public class UserLoginReqDto {
    /**
     * 登录提交的用户名
     */
    private String username;
    /**
     * 登录时提交的密码
     */
    private String password;
}
