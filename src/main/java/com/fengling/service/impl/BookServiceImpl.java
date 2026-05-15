package com.fengling.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.AuthUserInfo;
import com.fengling.common.context.UserContext;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.RedisUtil;
import com.fengling.entity.BookShelf;
import com.fengling.entity.dto.*;
import com.fengling.mapper.BookMapper;
import com.fengling.mapper.BookShelfMapper;
import com.fengling.mapper.ChapterMapper;
import com.fengling.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final ChapterMapper chapterMapper;
    private final BookShelfMapper bookShelfMapper;
    private final RedisUtil redisUtil;

    @Override
    public CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(Integer categoryId, PageReqDto pageReqDto) {
        //分页对象
        Page<BookListRespDto> page = new Page<>(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        Page<BookListRespDto> bookPage;
        if (categoryId == 0) {
            // 查询所有小说
            bookPage = bookMapper.selectAllNovelPage(page);
        } else {
            bookPage = bookMapper.selectCategoryNovelPage(page, categoryId);
        }
        // 压缩简介
        bookPage.getRecords().forEach(book ->
                book.setBookIntro(shortBookIntro(book.getBookIntro())));
        return CommonResult.success(PageRespDto.of(bookPage));
    }

    @Override
    public CommonResult<BookInfoRespDto> getBookInfoById(Long bookId) {
        BookInfoRespDto bookInfoRespDto = bookMapper.getBookInfoById(bookId);
        if (bookInfoRespDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }
        //获取是否在书架中
        AuthUserInfo authUserInfo = UserContext.getAuthUserInfo();
        if (authUserInfo != null) {
            BookShelf one = bookShelfMapper.selectOne(
                    new LambdaQueryWrapper<BookShelf>()
                            .eq(BookShelf::getUserId, authUserInfo.getUserId())
                            .eq(BookShelf::getBookId, bookId)
            );
            if (one != null) {
                bookInfoRespDto.setIsShelf(true);
            }
        }
        List<ChapterListRespDto> chapterList = chapterMapper.getChapterListByBookId(bookId);
        bookInfoRespDto.setChapterList(chapterList);
        return CommonResult.success(bookInfoRespDto);
    }

    @Override
    public CommonResult<ChapterContentRespDto> getBookContentById(Long bookId, Long chapterId) {
        ChapterContentRespDto chapterContentRespDto = chapterMapper.getBookContentById(bookId, chapterId);
        if (chapterContentRespDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说章节不存在");
        }
        chapterContentRespDto.setPreChapterId(
                chapterMapper.getPreChapterId(bookId, chapterContentRespDto.getChapterNum())
        );
        chapterContentRespDto.setNextChapterId(
                chapterMapper.getNextChapterId(bookId, chapterContentRespDto.getChapterNum())
        );

        AuthUserInfo authUserInfo = UserContext.getAuthUserInfo();
        if (authUserInfo != null) {
            //设置小说最后阅读到的章节id和最后阅读时间
            bookShelfMapper.update(
                    new BookShelf().setLastReadChapterId(chapterId)
                            .setLastReadTime(LocalDateTime.now()),
                    new LambdaQueryWrapper<BookShelf>()
                            .eq(BookShelf::getUserId, authUserInfo.getUserId())
                            .eq(BookShelf::getBookId, bookId)
            );
        }

        return CommonResult.success(chapterContentRespDto);
    }

    @Override
    public CommonResult<List<BookRecentListRespDto>> listRecentBookList() {
        // 从redis读取
        String jsonStr = redisUtil.getValueForKey(CacheConstants.NOVEL_RECENT);
        if (jsonStr != null && !jsonStr.isBlank()) {
            return CommonResult.success(JSONUtil.toList(jsonStr, BookRecentListRespDto.class));
        }
        List<BookRecentListRespDto> recentBookList = bookMapper.listRecentBookList(
                CommonConstants.NOVEL_RECENT_LIMIT
        );
        recentBookList.forEach(
                book -> book.setBookIntro(shortBookIntro(book.getBookIntro()))
        );
        // 将列表转换为字符串
        String valueStr = JSONUtil.toJsonStr(recentBookList);
        redisUtil.addRedisCache(
                CacheConstants.NOVEL_RECENT,
                valueStr,
                CacheConstants.NOVEL_RECENT_TTL
        );
        return CommonResult.success(recentBookList);
    }

    /**
     * 压缩小说简介
     *
     * @param bookIntro 小说简介
     * @return 压缩后的小说简介
     */
    private String shortBookIntro(String bookIntro) {
        if (bookIntro == null || bookIntro.isBlank() || bookIntro.length() <= 50) {
            return bookIntro;
        }
        return bookIntro.substring(0, 50) + "……";
    }
}
