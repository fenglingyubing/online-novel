package com.fengling.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengling.entity.BookReviews;
import com.fengling.entity.dto.BookReviewListRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookReviewsMapper extends BaseMapper<BookReviews> {

    /**
     * 小说评论列表查询
     *
     * @param page         分页对象
     * @param bookId       小说id
     * @param parentId     父评论id
     * @param reviewStatus 评论状态
     * @return 评论分页列表
     */
    Page<BookReviewListRespDto> listBookReviews(
            Page<BookReviewListRespDto> page,
            @Param("bookId") Long bookId,
            @Param("parentId") Long parentId,
            @Param("reviewStatus") Integer reviewStatus
    );
}
