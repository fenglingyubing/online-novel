package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.context.UserContext;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.BookShelf;
import com.fengling.entity.dto.BookShelfListReqDto;
import com.fengling.entity.dto.BookShelfRespDto;
import com.fengling.mapper.BookShelfMapper;
import com.fengling.service.BookShelfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookShelfServiceImpl implements BookShelfService {

    private final BookShelfMapper bookShelfMapper;

    @Override
    public CommonResult<PageRespDto<BookShelfRespDto>> listShelfNovels(PageReqDto pageReqDto) {
        Page<BookShelfRespDto> page = new Page<>(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        //获取用户id
        Long userId = UserContext.getUserId();
        Page<BookShelfRespDto> pageNovelsList = bookShelfMapper.listShelfNovels(page, userId);
        return CommonResult.success(PageRespDto.of(pageNovelsList));
    }

    @Override
    public CommonResult<Void> saveBookToBookShelf(Long bookId) {
        //获取用户id
        Long userId = UserContext.getUserId();
        bookShelfMapper.insert(new BookShelf(userId, bookId));
        return CommonResult.success();
    }

    @Override
    public CommonResult<Void> deleteBookById(BookShelfListReqDto bookShelfListReqDto) {
        //获取用户id
        Long userId = UserContext.getUserId();
        List<Long> bookIdList = bookShelfListReqDto.getBookIdList();
        bookShelfMapper.delete(
                new LambdaQueryWrapper<BookShelf>()
                        .eq(BookShelf::getUserId, userId)
                        .in(BookShelf::getBookId, bookIdList)
        );
        return CommonResult.success();
    }
}
