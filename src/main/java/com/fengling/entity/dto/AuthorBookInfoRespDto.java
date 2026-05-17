package com.fengling.entity.dto;


import com.fengling.common.dto.PageRespDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 作家下某作品详情响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorBookInfoRespDto {

    /**
     * 小说id
     */
    private Long id;

    /**
     * 小说名称
     */
    private String bookName;

    /**
     * 小说封面
     */
    private String coverUrl;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 发布状态（0-下架，1-上架）
     */
    private Integer publishStatus;

    /**
     * 更新状态（0-连载中，1-已完结）
     */
    private Integer updateStatus;

    /**
     * 小说简介
     */
    private String bookIntro;

    /**
     * 小说目录
     */
    private PageRespDto<BookChapterListRespDto> bookChapterList;
}
