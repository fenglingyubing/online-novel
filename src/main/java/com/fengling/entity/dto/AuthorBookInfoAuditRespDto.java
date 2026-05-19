package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 小说变更信息响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBookInfoAuditRespDto {

    /**
     * 审核id
     */
    private Long id;

    /**
     * 小说id
     */
    private Long bookId;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 变更-小说名
     */
    private String bookNameChange;

    /**
     * 变更-小说简介
     */
    private String bookIntro;

    /**
     * 变更-小说封面
     */
    private String coverUrl;

    /**
     * 发布状态
     */
    private Integer publishStatus;

    /**
     * 审核状态（0-待审核，1-已通过，2-已驳回）
     */
    private Integer auditStatus;

    /**
     * 审核人
     */
    private String adminName;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 审核通过时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 申请类型：（1-作品创建，2-信息变更和作品上架）
     */
    private Integer auditType;
}
