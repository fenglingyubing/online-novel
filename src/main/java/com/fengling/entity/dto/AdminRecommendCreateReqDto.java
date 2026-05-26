package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 创建推荐数据请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRecommendCreateReqDto {

    /**
     * 推荐小说id
     */
    private Long bookId;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 推荐类型
     */
    private Integer recommendType;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
