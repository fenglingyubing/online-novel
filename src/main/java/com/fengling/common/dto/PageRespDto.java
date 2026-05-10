package com.fengling.common.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageRespDto<T> {
    /**
     * 当前页数据
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页数量
     */
    private Long pageSize;

    /**
     * 总页数
     */
    private Long pages;

    public static <T> PageRespDto<T> of(Page<T> page) {
        return new PageRespDto<>(
                page.getRecords(),
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getPages()
        );
    }
}
