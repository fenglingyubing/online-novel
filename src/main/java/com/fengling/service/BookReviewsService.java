package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookReviewListRespDto;
import com.fengling.entity.dto.BookReviewSaveReqDto;

public interface BookReviewsService {

    /**
     * 评论列表查询
     *
     * @param bookId 小说id
     * @param pageReqDto 分页请求参数
     * @return 评论列表
     */
    CommonResult<PageRespDto<BookReviewListRespDto>> listBookReviews(Long bookId, PageReqDto pageReqDto);

    /**
     * 发表评论
     *
     * @param bookId 小说id
     * @param reqDto 评论发表请求参数
     * @return 无
     */
    CommonResult<Void> saveBookReview(Long bookId, BookReviewSaveReqDto reqDto);
}
