package com.fengling.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookCategory {
    /**
     * 小说分类id
     */
    private Integer id;
    /**
     * 父分类id
     */
    private Integer parentId;
    /**
     * 小说分类名称
     */
    private String categoryName;
    /**
     * 小说分类排序值，值越小越靠前
     */
    private Integer sort;
    /**
     * 小说分类状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
