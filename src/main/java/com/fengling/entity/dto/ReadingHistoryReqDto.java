package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 阅读历史更新请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingHistoryReqDto {

    /**
     * 章节id
     */
    private Long chapterId;

    /**
     * 章节名称
     */
    private String chapterName;
}
