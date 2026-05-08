package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
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
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
