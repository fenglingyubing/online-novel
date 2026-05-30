package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.UserContext;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.PageAuthUtil;
import com.fengling.entity.BookInfo;
import com.fengling.entity.BookReviews;
import com.fengling.entity.dto.BookReviewListRespDto;
import com.fengling.entity.dto.BookReviewSaveReqDto;
import com.fengling.mapper.BookMapper;
import com.fengling.mapper.BookReviewsMapper;
import com.fengling.service.BookReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookReviewsServiceImpl implements BookReviewsService {

    private final BookReviewsMapper bookReviewsMapper;
    private final BookMapper bookMapper;
    private final PageAuthUtil pageAuthUtil;

    @Override
    public CommonResult<PageRespDto<BookReviewListRespDto>> listBookReviews(Long bookId, PageReqDto pageReqDto) {
        pageAuthUtil.pageAuth(pageReqDto);
        checkBookExists(bookId);
        Page<BookReviewListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<BookReviewListRespDto> pageBookReviews = bookReviewsMapper.listBookReviews(
                page,
                bookId,
                CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT,
                CommonConstants.BOOK_REVIEW_STATUS_NORMAL
        );
        return CommonResult.success(PageRespDto.of(pageBookReviews));
    }

    @Override
    public CommonResult<PageRespDto<BookReviewListRespDto>> listBookReviewReplies(
            Long bookId,
            Long parentId,
            PageReqDto pageReqDto
    ) {
        pageAuthUtil.pageAuth(pageReqDto);
        checkBookExists(bookId);
        checkParentReviewExists(bookId, parentId);
        Page<BookReviewListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<BookReviewListRespDto> pageBookReviews = bookReviewsMapper.listBookReviews(
                page,
                bookId,
                parentId,
                CommonConstants.BOOK_REVIEW_STATUS_NORMAL
        );
        return CommonResult.success(PageRespDto.of(pageBookReviews));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Void> saveBookReview(Long bookId, BookReviewSaveReqDto reqDto) {
        checkBookReviewParam(bookId, reqDto);
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }

        BookReviews bookReviews = new BookReviews();
        bookReviews.setUserId(userId);
        bookReviews.setBookId(bookId);
        bookReviews.setReviewContent(reqDto.getReviewContent().trim());
        bookReviews.setParentId(reqDto.getParentId() == null ? CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT : reqDto.getParentId());
        bookReviews.setStars(bookReviews.getParentId().equals(CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT) ? reqDto.getStars() : null);
        bookReviews.setLikeCount(CommonConstants.BOOK_REVIEW_DEFAULT_COUNT);
        bookReviews.setReplyCount(CommonConstants.BOOK_REVIEW_DEFAULT_COUNT);
        bookReviews.setReviewStatus(CommonConstants.BOOK_REVIEW_STATUS_NORMAL);
        bookReviews.setCreateTime(LocalDateTime.now());
        bookReviews.setUpdateTime(LocalDateTime.now());
        bookReviewsMapper.insert(bookReviews);

        if (!bookReviews.getParentId().equals(CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT)) {
            bookReviewsMapper.update(null,
                    new LambdaUpdateWrapper<BookReviews>()
                            .eq(BookReviews::getId, bookReviews.getParentId())
                            .setSql("reply_count = reply_count + 1")
            );
        }
        return CommonResult.success();
    }

    private void checkBookReviewParam(Long bookId, BookReviewSaveReqDto reqDto) {
        if (bookId == null || reqDto == null || reqDto.getReviewContent() == null || reqDto.getReviewContent().trim().isEmpty()) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        checkBookExists(bookId);
        Long parentId = reqDto.getParentId() == null ? CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT : reqDto.getParentId();
        if (parentId.equals(CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT)) {
            if (reqDto.getStars() == null || reqDto.getStars() < CommonConstants.BOOK_REVIEW_MIN_STARS || reqDto.getStars() > CommonConstants.BOOK_REVIEW_MAX_STARS) {
                throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID, "评分必须在1到5之间");
            }
            return;
        }
        BookReviews parentReview = bookReviewsMapper.selectOne(
                new LambdaQueryWrapper<BookReviews>()
                        .eq(BookReviews::getId, parentId)
                        .eq(BookReviews::getBookId, bookId)
                        .eq(BookReviews::getParentId, CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT)
                        .eq(BookReviews::getReviewStatus, CommonConstants.BOOK_REVIEW_STATUS_NORMAL)
        );
        if (parentReview == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "父评论不存在");
        }
    }

    private void checkBookExists(Long bookId) {
        if (bookId == null || bookId <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        BookInfo bookInfo = bookMapper.selectById(bookId);
        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说信息不存在");
        }
    }

    private void checkParentReviewExists(Long bookId, Long parentId) {
        if (parentId == null || parentId <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        BookReviews parentReview = bookReviewsMapper.selectOne(
                new LambdaQueryWrapper<BookReviews>()
                        .select(BookReviews::getId)
                        .eq(BookReviews::getId, parentId)
                        .eq(BookReviews::getBookId, bookId)
                        .eq(BookReviews::getParentId, CommonConstants.BOOK_REVIEW_PARENT_ID_ROOT)
                        .eq(BookReviews::getReviewStatus, CommonConstants.BOOK_REVIEW_STATUS_NORMAL)
        );
        if (parentReview == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "父评论不存在");
        }
    }
}
