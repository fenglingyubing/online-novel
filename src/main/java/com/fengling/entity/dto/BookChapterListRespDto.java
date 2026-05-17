package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作家作品详情章节列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookChapterListRespDto {

    /**
     * 章节id
     */
    private Long id;

    /**
     * 章节名称
     */
    private String chapterName;

    /**
     * 章节序号
     */
    private Integer chapterNum;

    /**
     * 章节字数
     */
    private Integer wordCount;

    /**
     * 章节状态
     */
    private Integer chapterStatus;
}
