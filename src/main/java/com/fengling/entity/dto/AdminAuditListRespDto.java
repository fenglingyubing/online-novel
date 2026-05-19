package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员后台审核列表信息查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditListRespDto {

    /**
     * 审核变更信息id
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
     * 作家笔名
     */
    private String authorName;

    /**
     * 小说名称
     */
    private String bookName;

    /**
     * 小说封面
     */
    private String coverUrl;

    /**
     * 提交时间
     */
    private LocalDateTime subTime;

    /**
     * 审核类型（1-新建作品，2-信息变更和上架）
     */
    private Integer auditType;
}
