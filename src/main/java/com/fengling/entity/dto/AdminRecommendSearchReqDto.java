package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRecommendSearchReqDto {

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 作家名
     */
    private String authorName;
}
