package com.fengling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 阅读历史实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingHistory {

    /**
     * 阅读历史id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 小说id
     */
    private Long bookId;

    /**
     * 最后阅读章节id
     */
    private Long lastChapterId;

    /**
     * 最后阅读章节名
     */
    private String lastChapterName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
