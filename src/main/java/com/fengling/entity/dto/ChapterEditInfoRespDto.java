package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编辑章节响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterEditInfoRespDto {

    /**
     * 章节id
     */
    private Long id;

    /**
     * 小说名称
     */
    private String bookName;

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
}
