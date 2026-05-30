package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 阅读历史列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingHistoryRespDto {

    /**
     * 阅读历史id
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
     * 最后阅读章节id
     */
    private Long lastChapterId;

    /**
     * 最后阅读章节名称
     */
    private String lastChapterName;

    /**
     * 最后阅读时间
     */
    private LocalDateTime updateTime;
}
