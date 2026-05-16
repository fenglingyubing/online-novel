package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 章节新增参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterSaveReqDto {

    /**
     * 章节名称
     */
    private String chapterName;

    /**
     * 章节正文
     */
    private String chapterContent;

    /**
     * 章节状态（0-草稿，1-已发布，2-下架，3-审核中）
     */
    private Integer chapterStatus;
}
