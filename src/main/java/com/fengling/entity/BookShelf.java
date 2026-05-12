package com.fengling.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BookShelf {
    /**
     * 书架id
     */
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
     * 上次阅读章节id
     */
    private Long lastReadChapterId;
    /**
     * 上次阅读时间
     */
    private LocalDateTime lastReadTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    public BookShelf(Long userId, Long bookId){
        this.userId = userId;
        this.bookId = bookId;
    }
}
