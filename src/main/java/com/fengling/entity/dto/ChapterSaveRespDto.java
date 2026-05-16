package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增章节后章节id响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterSaveRespDto {

    /**
     * 章节id
     */
    private Long id;
}
