package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRespDto {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户状态
     */
    private Integer userStatus;
    /**
     * 用户角色
     */
    private Integer userRole;
    /**
     * 用户余额
     */
    private Integer userBalance;
    /**
     * 登录token
     */
    private String token;
}
