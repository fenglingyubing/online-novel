package com.fengling.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookShelfRespDto {
    /**
     * 书架id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 小说id
     */
    private Long bookId;
    /**
     * 小说名称
     */
    private String bookName;
    /**
     * 小说封面链接
     */
    private String coverUrl;
    /**
     * 第几章
     */
    private Integer lastChapterNum;
    /**
     * 一共有多少章
     */
    private Integer chapterCount;
    /**
     * 上次阅读章节id
     */
    private Long lastReadChapterId;
    /**
     * 上次阅读时间
     */
    private LocalDateTime lastReadTime;

}
