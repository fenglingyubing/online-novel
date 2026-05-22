package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员信息审核：信息变更详情响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditInfoRespDto {

    /**
     * 变更id
     */
    private Long id;

    /**
     * 所属小说名
     */
    private String bookName;

    /**
     * 提交人
     */
    private String authorName;

    /**
     * 变更信息：小说名
     */
    private String bookNameChange;

    /**
     * 变更信息：小说简介
     */
    private String bookIntro;

    /**
     * 变更信息：小说封面
     */
    private String coverUrl;

    /**
     * 变更信息：发布状态
     */
    private Integer publishStatus;

    /**
     * 变更类型
     */
    private Integer auditType;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 提交时间
     */
    private LocalDateTime subTime;
}
