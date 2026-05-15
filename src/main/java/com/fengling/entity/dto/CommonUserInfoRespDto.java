package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前登录用户公共信息响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonUserInfoRespDto {
    /**
     * 用户性别
     */
    private Integer userSex;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private String userPhoto;
    /**
     * 用户余额
     */
    private Integer userBalance;
}
