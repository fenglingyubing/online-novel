package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小说信息更新-无需审核响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBookInfoNotAuditReqDto {

    /**
     * 发布状态
     */
    private Integer publishStatus;

    /**
     * 更新状态
     */
    private Integer updateStatus;
}
