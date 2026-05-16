package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 作家后台草稿箱响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDraftsRespDto {

    /**
     * 草稿id
     */
    private Long id;

    /**
     * 章节名称
     */
    private String chapterName;

    /**
     * 小说名称
     */
    private String bookName;

    /**
     * 章节正文
     */
    private String chapterContent;

    /**
     * 章节字数
     */
    private Integer wordCount;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
