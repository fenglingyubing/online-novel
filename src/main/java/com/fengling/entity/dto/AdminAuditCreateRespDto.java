package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员审核新书：查询新书详情响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditCreateRespDto {

    /**
     * 审核id
     */
    private Long id;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 作家id
     */
    private Long authorId;

    /**
     * 作家名
     */
    private String authorName;

    /**
     * 审核人昵称
     */
    private String auditAdminName;

    /**
     * 小说简介
     */
    private String bookIntro;

    /**
     * 小说封面
     */
    private String coverUrl;

    /**
     * 审核类型
     */
    private Integer auditType;

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
