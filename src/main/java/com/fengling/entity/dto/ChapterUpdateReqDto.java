package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 章节内容修改请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterUpdateReqDto {

    /**
     * 章节名称
     */
    private String chapterName;

    /**
     * 章节正文
     */
    private String chapterContent;

    /**
     * 章节状态
     */
    private Integer chapterStatus;
}
