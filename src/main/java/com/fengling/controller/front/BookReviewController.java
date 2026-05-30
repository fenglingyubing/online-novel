package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookReviewListRespDto;
import com.fengling.entity.dto.BookReviewSaveReqDto;
import com.fengling.service.BookReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 评论列表查询
     *
     * @param bookId 小说id
     * @param pageReqDto 分页请求参数
     * @return 评论列表
     */
    @GetMapping("/{bookId}" + ApiPathConstants.LIST)
    public CommonResult<PageRespDto<BookReviewListRespDto>> listBookReviews(
            @PathVariable("bookId") Long bookId,
            PageReqDto pageReqDto
    ) {
        return bookReviewsService.listBookReviews(bookId, pageReqDto);
    }

    /**
     * 评论回复列表查询
     *
     * @param bookId 小说id
     * @param parentId 父评论id
     * @param pageReqDto 分页请求参数
     * @return 评论回复列表
     */
    @GetMapping("/{bookId}/{parentId}/reply" + ApiPathConstants.LIST)
    public CommonResult<PageRespDto<BookReviewListRespDto>> listBookReviewReplies(
            @PathVariable("bookId") Long bookId,
            @PathVariable("parentId") Long parentId,
            PageReqDto pageReqDto
    ) {
        return bookReviewsService.listBookReviewReplies(bookId, parentId, pageReqDto);
    }

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
