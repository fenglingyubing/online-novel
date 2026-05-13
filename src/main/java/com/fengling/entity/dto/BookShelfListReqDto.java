package com.fengling.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookShelfListReqDto {
    /**
     * 小说id
     */
    private List<Long> bookIdList;
}
