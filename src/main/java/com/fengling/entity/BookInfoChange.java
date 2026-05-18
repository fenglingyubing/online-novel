package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 小说变更信息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoChange {

    /**
     * 变更信息
     */
    private Long id;

    /**
     * 小说id
     */
    private Long bookId;

    /**
     * 作家id
     */
    private Long authorId;

    /**
     * 审核管理员id
     */
    private Long auditAdminId;

    /**
     * 变更小说名
     */
    private String bookName;

    /**
     * 变更小说简介
     */
    private String bookIntro;

    /**
     * 变更小说封面
     */
    private String coverUrl;

    /**
     * 发布状态（只有下架到上架才需要审核 0->1 ）
     */
    private Integer publishStatus;

    /**
     * 审核状态（0-待审核，1-已通过，2-已驳回）
     */
    private Integer auditStatus;

    /**
     * 应用状态（0-未应用，1-已应用）
     */
    private Integer applyStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
