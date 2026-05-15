package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 小说列表响应结果
 */
@Data
@AllArgsConstructor
public class BookListRespDto {
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
     * 作者名字
     */
    private String authorName;
    /**
     * 分类id
     */
    private Integer categoryId;
    /**
     * 更新状态（0-连载中，1-已完结）
     */
    private Integer updateStatus;
    /**
     * 小说简介（截断后）
     */
    private String bookIntro;
    /**
     * 小说字数
     */
    private Integer wordCount;
    /**
     * 最新章节id
     */
    private Long latestChapterId;

    /**
     * 最新章节名称
     */
    private String latestChapterName;
}
