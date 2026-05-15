package com.fengling.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户个人信息修改请求参数
 */
@Data
@NoArgsConstructor
public class UserInfoMineReqDto {
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
     * 修改时间
     */
    private LocalDateTime updateTime;
}
