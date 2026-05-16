package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 作家作品管理页面响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorNovelsListRespDto {

    /**
     * 小说id
     */
    private Long id;
    /**
     * 小说名称
     */
    private String bookName;
    /**
     * 小说封面
     */
    private String coverUrl;
    /**
     * 更新状态（0-连载中，1-已完结）
     */
    private Integer updateStatus;
    /**
     * 小说字数
     */
    private Integer wordCount;
    /**
     * 最新章节名称
     */
    private String latestChapterName;
    /**
     * 最新章节更新时间
     */
    private LocalDateTime lastChapterTime;
}
