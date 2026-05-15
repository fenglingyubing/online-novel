package com.fengling.entity.dto;

import lombok.Data;

/**
 * 作家注册请求参数
 */
@Data
public class AuthorReqDto {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 作家笔名
     */
    private String authorName;
}
