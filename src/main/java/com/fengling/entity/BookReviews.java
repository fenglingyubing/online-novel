package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReviews {

    /**
     * 评论id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 小说id
     */
    private Long bookId;

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

    /**
     * 点赞总数
     */
    private Integer likeCount;

    /**
     * 回复总数
     */
    private Integer replyCount;

    /**
     * 评论状态（0-正常，1-禁用）
     */
    private Integer reviewStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
