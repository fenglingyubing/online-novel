package com.fengling.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageReqDto {
    /**
     * 当前页码，从1开始
     */
    private Long pageNum = 1L;
    /**
     * 每页数量
     */
    private Long pageSize = 10L;
}
