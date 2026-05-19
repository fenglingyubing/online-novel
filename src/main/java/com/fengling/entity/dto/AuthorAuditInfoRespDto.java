package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审核变更信息详情响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorAuditInfoRespDto {

    /**
     * 审核id
     */
    private Long id;

    /**
     * 所属小说名
     */
    private String bookName;

    /**
     * 变更小说名
     */
    private String bookNameChange;

    /**
     * 变更小说简介
     */
    private String bookIntro;

    /**
     * 变更封面
     */
    private String coverUrl;

    /**
     * 发布状态
     */
    private Integer publishStatus;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核类型
     */
    private Integer auditType;

    /**
     * 审核留言
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    private String auditName;
}
