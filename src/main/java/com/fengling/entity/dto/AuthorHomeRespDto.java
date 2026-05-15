package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作家主页响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorHomeRespDto {
    /**
     * 作家id
     */
    private Long id;
    /**
     * 作者笔名
     */
    private String authorName;
    /**
     * 用户头像
     */
    private String userPhoto;
    /**
     * 累计字数
     */
    private Integer wordCount;
    /**
     * 最近更新小说
     */
    private AuthorRecentNovelRespDto recentNovelRespDto;
}
