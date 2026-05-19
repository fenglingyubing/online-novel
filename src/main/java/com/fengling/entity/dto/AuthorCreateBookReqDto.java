package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作家新建作品参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreateBookReqDto {

    /**
     * 小说名
     */
    private String bookName;

    /**
     * 小说简介
     */
    private String bookIntro;

    /**
     * 分类id
     */
    private Integer categoryId;
}
