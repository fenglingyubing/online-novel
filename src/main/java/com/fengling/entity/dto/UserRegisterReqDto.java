package com.fengling.entity.dto;

import lombok.Data;

@Data
public class UserRegisterReqDto {
    /**
     * 注册用户名
     */
    private String username;
    /**
     * 注册密码
     */
    private String password;
}
