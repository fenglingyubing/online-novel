package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户封禁信息实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDisableInfo {

    /**
     * 封禁id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 封禁原因
     */
    private String disableInfo;

    /**
     * 封禁天数（-1表示永久封禁）
     */
    private Integer disableDays;

    /**
     * 操作人员id
     */
    private Long disableAdminId;

    /**
     * 封禁备注
     */
    private String disableRemark;

    /**
     * 封禁起始时间
     */
    private LocalDateTime disableStartTime;

    /**
     * 封禁结束时间
     */
    private LocalDateTime disableEndTime;

    /**
     * 封禁状态
     */
    private Integer disableStatus;

    /**
     * 解封时间
     */
    private LocalDateTime enableTime;

    /**
     * 解封管理员id
     */
    private Long enableAdminId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
