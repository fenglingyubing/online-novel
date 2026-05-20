package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 章节审核实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterAudit {

    /**
     * 审核id
     */
    private Long id;

    /**
     * 章节id
     */
    private Long chapterId;

    /**
     * 小说id
     */
    private Long bookId;

    /**
     * 作家id
     */
    private Long authorId;

    /**
     * 审核人id
     */
    private Long auditAdminId;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 应用状态
     */
    private Integer applyStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
