package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInfo {
    /**
     * 作者id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 作者笔名
     */
    private String authorName;
    /**
     * 作者简介
     */
    private String authorIntro;
    /**
     * 作者状态
     */
    private Integer authorStatus;
    /**
     * 作品数
     */
    private Integer bookCount;
    /**
     * 总字数
     */
    private Integer wordCount;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    public AuthorInfo(String authorName, Long userId){
        this.authorName = authorName;
        this.userId = userId;
    }
}
