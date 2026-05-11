package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.BookInfo;
import com.fengling.entity.dto.BookInfoRespDto;
import com.fengling.entity.dto.BookListRespDto;
import com.fengling.entity.dto.ChapterContentRespDto;
import com.fengling.entity.dto.ChapterListRespDto;
import com.fengling.mapper.AuthorMapper;
import com.fengling.mapper.BookMapper;
import com.fengling.mapper.ChapterMapper;
import com.fengling.service.BookService;
import io.netty.util.internal.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final ChapterMapper chapterMapper;

    @Override
    public CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(Integer categoryId, PageReqDto pageReqDto) {
        //分页对象
        Page<BookListRespDto> page = new Page<>(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        Page<BookListRespDto> bookPage = bookMapper.selectCategoryNovelPage(page, categoryId);
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
        List<ChapterListRespDto> chapterList = chapterMapper.getChapterListByBookId(bookId);
        bookInfoRespDto.setChapterList(chapterList);
        return CommonResult.success(bookInfoRespDto);
    }

    @Override
    public CommonResult<ChapterContentRespDto> getBookContentById(Long bookId, Long chapterId) {
        ChapterContentRespDto chapterContentRespDto = chapterMapper.getBookContentById(bookId, chapterId);
        chapterContentRespDto.setPreChapterId(
                chapterMapper.getPreChapterId(bookId, chapterContentRespDto.getChapterNum())
        );
        chapterContentRespDto.setNextChapterId(
                chapterMapper.getNextChapterId(bookId, chapterContentRespDto.getChapterNum())
        );
        return CommonResult.success(chapterContentRespDto);
    }

    /**
     * 压缩小说简介
     *
     * @param bookIntro 小说简介
     * @return 压缩后的小说简介
     */
    private String shortBookIntro(String bookIntro) {
        if (StringUtil.isNullOrEmpty(bookIntro) || bookIntro.length() <= 50) {
            return bookIntro;
        }
        return bookIntro.substring(0, 50) + "……";
    }
}
