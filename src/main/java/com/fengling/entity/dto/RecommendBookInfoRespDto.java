package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页推荐书籍查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendBookInfoRespDto {

    /**
     * 小说id
     */
    private Long id;

    /**
     * 小说名称
     */
    private String bookName;

    /**
     * 小说简介
     */
    private String bookIntro;

    /**
     * 小说封面
     */
    private String coverUrl;
}
