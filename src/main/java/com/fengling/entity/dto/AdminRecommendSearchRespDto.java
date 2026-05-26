package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 推荐位置搜索响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRecommendSearchRespDto {

    /**
     * 小说id
     */
    private Long id;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 作家名
     */
    private String authorName;

    /**
     *分类id
     */
    private Integer categoryId;
}
