package com.fengling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告信息实体
 */
@Data
public class AnnouncementInfo {
    /**
     * 公告id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发布人id
     */
    private Long publisherId;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告详情
     */
    private String content;

    /**
     * 发布状态（0-发布，1-草稿，2-下架）
     */
    private Integer publishStatus;

    /**
     * 公告类型（0-全体，1-作者，2-读者）
     */
    private Integer announcementType;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
