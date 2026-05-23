package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 章节审核信息详情查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditChapterRespDto {

    /**
     * 审核id
     */
    private Long id;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 章节名
     */
    private String chapterName;

    /**
     * 章节正文
     */
    private String chapterContent;

    /**
     * 字数
     */
    private Integer wordCount;

    /**
     * 作家id
     */
    private Long authorId;

    /**
     * 作家笔名
     */
    private String authorName;

    /**
     * 审核人昵称
     */
    private String auditAdminName;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 提交时间
     */
    private LocalDateTime subTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
}
