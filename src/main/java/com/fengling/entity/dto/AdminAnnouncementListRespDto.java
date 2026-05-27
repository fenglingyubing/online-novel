package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 公告列表响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAnnouncementListRespDto {

    /**
     * 公告id
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 推送群体类型
     */
    private Integer announcementType;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 发布状态
     */
    private Integer publishStatus;
}
