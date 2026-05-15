package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 最新上架小说列表响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRecentListRespDto {
    /**
     * 小说id
     */
    private Long id;
    /**
     * 小说名称
     */
    private String bookName;
    /**
     * 小说封面链接
     */
    private String coverUrl;
    /**
     * 作者名称
     */
    private String authorName;
    /**
     * 小说简介
     */
    private String bookIntro;
    /**
     * 分类名称
     */
    private String categoryName;
}
