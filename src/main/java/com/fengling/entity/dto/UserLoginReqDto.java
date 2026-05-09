package com.fengling.entity.dto;

import lombok.Data;

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
