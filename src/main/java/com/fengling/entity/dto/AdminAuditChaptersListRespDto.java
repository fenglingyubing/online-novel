package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 章节审核列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuditChaptersListRespDto {

    /**
     * 章节id
     */
    private Long id;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 作家名
     */
    private String authorName;

    /**
     * 章节名
     */
    private String chapterName;

    /**
     * 章节字数
     */
    private Integer wordCount;

    /**
     * 提交时间
     */
    private LocalDateTime subTime;
}
