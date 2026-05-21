package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息变更和新书创建审核请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditInfoReqDto {

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;
}
