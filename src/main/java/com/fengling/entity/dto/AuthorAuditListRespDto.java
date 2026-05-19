package com.fengling.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 作家审核章节响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorAuditListRespDto {

    /**
     * 章节id
     */
    private Long id;

    /**
     * 小说id
     */
    private Long bookId;

    /**
     * 章节名
     */
    private String chapterName;

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 章节字数
     */
    private Integer wordCount;

    /**
     * 申请时间
     */
    private LocalDateTime applicationTime;
}
