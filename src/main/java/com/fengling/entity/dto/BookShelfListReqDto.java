package com.fengling.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 书架批量操作请求参数
 */
@Data
public class BookShelfListReqDto {
    /**
     * 小说id列表
     */
    private List<Long> bookIdList;
}
