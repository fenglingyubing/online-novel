package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户个人信息响应结果
 */
@Data
@AllArgsConstructor
public class UserInfoMineRespDto {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户性别
     */
    private Integer userSex;
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
     * 用户书币余额
     */
    private Integer userBalance;
    /**
     * 用户状态
     */
    private Integer userStatus;
    /**
     * 用户加入时间
     */
    private LocalDateTime createTime;
}
