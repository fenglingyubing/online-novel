package com.fengling.entity.dto;

import lombok.Data;

/**
 * 章节目录响应结果
 */
@Data
public class ChapterListRespDto {
    /**
     * 章节id
     */
    private Long id;

    /**
     * 章节名称
     */
    private String chapterName;
}
