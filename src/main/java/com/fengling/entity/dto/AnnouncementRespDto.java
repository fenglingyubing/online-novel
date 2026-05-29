package com.fengling.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 作家后台公告响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRespDto {

    /**
     * 公告id
     */
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
}
