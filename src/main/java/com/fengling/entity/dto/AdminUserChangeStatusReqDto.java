package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户状态更新请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserChangeStatusReqDto {

    /**
     * 封禁原因
     */
    private String disableInfo;

    /**
     * 封禁时长
     */
    private Integer disableDays;

    /**
     * 封禁备注
     */
    private String disableRemark;
}
