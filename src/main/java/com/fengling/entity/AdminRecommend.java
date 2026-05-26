package com.fengling.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员推荐实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRecommend {

    /**
     * 推荐id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 推荐人id
     */
    private Long adminId;

    /**
     * 推荐小说id
     */
    private Long bookId;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 推荐状态
     */
    private Integer recommendStatus;

    /**
     * 推荐类型
     */
    private Integer recommendType;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 推荐开始时间
     */
    private LocalDateTime startTime;

    /**
     * 推荐结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
