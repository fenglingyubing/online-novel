package com.fengling.entity.dto;

import lombok.Data;

@Data
public class ChapterContentRespDto {
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
     * 上一章id
     */
    private Long preChapterId;

    /**
     * 下一章id
     */
    private Long nextChapterId;
}
