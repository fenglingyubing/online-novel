package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookReviewSaveReqDto;

public interface BookReviewsService {

    /**
     * 发表评论
     *
     * @param bookId 小说id
     * @param reqDto 评论发表请求参数
     * @return 无
     */
    CommonResult<Void> saveBookReview(Long bookId, BookReviewSaveReqDto reqDto);
}
