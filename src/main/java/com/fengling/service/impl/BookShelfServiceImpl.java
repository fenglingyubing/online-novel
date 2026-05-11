package com.fengling.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.BookShelf;
import com.fengling.entity.dto.BookShelfRespDto;
import com.fengling.mapper.BookShelfMapper;
import com.fengling.service.BookShelfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookShelfServiceImpl implements BookShelfService {

    private final BookShelfMapper bookShelfMapper;

    @Override
    public CommonResult<PageRespDto<BookShelfRespDto>> listShelfNovels(Long userId, PageReqDto pageReqDto) {
        log.info("进入-------------------------------------------");
        Page<BookShelfRespDto> page = new Page<>(pageReqDto.getPageNum(), pageReqDto.getPageSize());
        Page<BookShelfRespDto> pageNovelsList = bookShelfMapper.listShelfNovels(page, userId);
        return CommonResult.success(PageRespDto.of(pageNovelsList));
    }

    @Override
    public CommonResult<Void> saveBookToBookShelf(Long userId, Long bookId) {
        bookShelfMapper.insert(new BookShelf(userId, bookId));
        return CommonResult.success();
    }
}
