package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 新书审核列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditCreateListRespDto {

    /**
     * 新书审核id
     */
    private Long id;

    /**
     * 小说id
     */
    private Long bookId;

    /**
     * 小说名称
     */
    private String bookName;

    /**
     * 小说封面
     */
    private String coverUrl;

    /**
     * 作家id
     */
    private Long authorId;

    /**
     * 作家笔名
     */
    private String authorName;

    /**
     * 提交时间
     */
    private LocalDateTime subTime;
}
