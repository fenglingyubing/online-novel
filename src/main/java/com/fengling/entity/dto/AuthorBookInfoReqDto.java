package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 书籍信息更新请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBookInfoReqDto {

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 小说简介
     */
    private String bookIntro;
}
