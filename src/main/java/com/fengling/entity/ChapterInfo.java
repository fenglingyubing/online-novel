package com.fengling.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChapterInfo {
    /**
     * 章节id
     */
    private Long id;
    /**
     * 小说id
     */
    private Long bookId;
    /**
     * 章节序号
     */
    private Integer chapterNum;
    /**
     * 章节名称
     */
    private String chapterName;
    /**
     * 章节正文
     */
    private String chapterContent;
    /**
     * 章节字数
     */
    private Integer wordCount;
    /**
     * 章节状态（0-草稿，1-已发布，2-下架，3-审核中）
     */
    private Integer chapterStatus;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
