package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员后台用户管理列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserManageListRespDto {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 用户头像
     */
    private String userPhoto;

    /**
     * 用户余额
     */
    private Integer userBalance;

    /**
     * 注册时间
     */
    private LocalDateTime createTime;

    /**
     * 用户状态
     */
    private Integer userStatus;
}
