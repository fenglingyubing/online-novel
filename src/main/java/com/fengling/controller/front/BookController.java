package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookListRespDto;
import com.fengling.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPathConstants.NOVEL)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/{categoryId}" + ApiPathConstants.LIST)
    public CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(
            @PathVariable("categoryId") Integer categoryId,
            PageReqDto pageReqDto
    ) {
        return bookService.listCategoryNovel(categoryId, pageReqDto);
    }
}
