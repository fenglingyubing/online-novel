package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编辑页小说列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorEditBookListResp {

    /**
     * 小说id
     */
    private Long id;

    /**
     * 小说名称
     */
    private String bookName;
}
