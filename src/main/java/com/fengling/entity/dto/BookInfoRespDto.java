package com.fengling.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookInfoRespDto {
    /**
     * 小说id
     */
    private Long id;
    /**
     * 小说名称
     */
    private String bookName;
    /**
     * 小说封面链接
     */
    private String coverUrl;
    /**
     * 作者名称
     */
    private String authorName;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 更新状态（0-连载中，1-已完结）
     */
    private Integer updateStatus;
    /**
     * 小说简介
     */
    private String bookIntro;
    /**
     * 章节数量
     */
    private Integer chapterCount;
    /**
     * 小说字数
     */
    private Integer wordCount;
    /**
     * 最新章节id
     */
    private Long latestChapterId;
    /**
     * 最新章节名称
     */
    private String latestChapterName;
    /**
     * 是否加入书架
     */
    private Boolean isShelf = false;
    /**
     * 最新章节更新时间
     */
    private LocalDateTime lastChapterTime;
    /**
     * 目录列表
     */
    private List<ChapterListRespDto> chapterList;
}
