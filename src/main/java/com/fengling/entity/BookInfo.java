package com.fengling.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookInfo {
    /**
     * 小说id
     */
    private Long id;
    /**
     * 小说名字
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
     * 分类id
     */
    private Integer categoryId;
    /**
     * 发布状态（0-下架，1-上架）
     */
    private Integer publishStatus;
    /**
     * 更新状态（0-连载中，1-已完结）
     */
    private Integer updateStatus;
    /**
     * 小说简介
     */
    private String bookIntro;
    /**
     * 小说章节数
     */
    private Integer chapterCount;
    /**
     * 小说字数
     */
    private Integer wordCount;
    /**
     * 最新章节ID
     */
    private Long latestChapterId;
    /**
     * 最新章节名称
     */
    private String latestChapterName;
    /**
     * 最新章节更新时间
     */
    private LocalDateTime lastChapterTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
