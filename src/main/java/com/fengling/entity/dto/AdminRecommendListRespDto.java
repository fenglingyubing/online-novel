package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 推荐列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRecommendListRespDto {

    /**
     * 推荐id
     */
    private Long id;

    /**
     * 推荐类型
     */
    private Integer recommendType;

    /**
     * 小说id
     */
    private Long bookId;

    /**
     * 小说封面
     */
    private String coverUrl;

    /**
     * 小说名称
     */
    private String bookName;

    /**
     * 作家名
     */
    private String authorName;

    /**
     * 起始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 推荐状态
     */
    private Integer recommendStatus;
}
