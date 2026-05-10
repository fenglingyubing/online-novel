package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.BookInfo;
import com.fengling.entity.dto.BookListRespDto;
import com.fengling.mapper.AuthorMapper;
import com.fengling.mapper.BookMapper;
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

    @Override
    public CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(Integer categoryId, PageReqDto pageReqDto) {
        //分页对象
        Page<BookListRespDto> page = new Page<>(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        Page<BookListRespDto> bookPage = bookMapper.selectCategoryNovelPage(page, categoryId);
        bookPage.getRecords().forEach(book ->
                book.setBookIntro(shortBookIntro(book.getBookIntro())));
        return CommonResult.success(PageRespDto.of(bookPage));
    }

    /**
     * 压缩小说简介
     *
     * @param bookIntro 小说简介
     * @return 压缩后的小说简介
     */
    private String shortBookIntro(String bookIntro) {
        if (StringUtil.isNullOrEmpty(bookIntro) || bookIntro.length() <= 25) {
            return bookIntro;
        }
        return bookIntro.substring(0, 25) + "……";
    }
}
