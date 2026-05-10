package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
