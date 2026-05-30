package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小说评论发表请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReviewSaveReqDto {

    /**
     * 评论内容
     */
    private String reviewContent;

    /**
     * 评分
     */
    private Integer stars;

    /**
     * 父评论id，0表示主评论
     */
    private Long parentId;
}
