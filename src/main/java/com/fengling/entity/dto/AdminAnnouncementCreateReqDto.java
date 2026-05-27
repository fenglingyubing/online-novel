package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公告创建请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAnnouncementCreateReqDto {

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型
     */
    private Integer announcementType;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 发布状态
     */
    private Integer publishStatus;
}
