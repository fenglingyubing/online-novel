package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookReviewLikes {
    /**
     * 点赞id
     */
    private Long id;

    /**
     * 评论id
     */
    private Long reviewId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
