package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookReviewSaveReqDto;
import com.fengling.service.BookReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPathConstants.BOOK_REVIEWS)
@RequiredArgsConstructor
public class BookReviewController {

    private final BookReviewsService bookReviewsService;

    /**
     * 发表评论
     *
     * @param bookId 小说id
     * @param reqDto 评论发表请求参数
     * @return 无
     */
    @PostMapping("/{bookId}")
    public CommonResult<Void> saveBookReview(
            @PathVariable("bookId") Long bookId,
            @RequestBody BookReviewSaveReqDto reqDto
    ) {
        return bookReviewsService.saveBookReview(bookId, reqDto);
    }
}
