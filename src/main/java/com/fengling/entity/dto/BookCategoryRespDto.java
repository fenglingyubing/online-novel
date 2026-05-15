package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小说分类响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryRespDto {
    /**
     * 小说分类id
     */
    private Integer id;
    /**
     * 小说分类名称
     */
    private String categoryName;
}
