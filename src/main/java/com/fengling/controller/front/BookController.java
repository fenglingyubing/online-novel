package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookInfoRespDto;
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

    /**
     * 查询分类下的小说（分页）
     *
     * @param categoryId 分类id
     * @param pageReqDto 分页实体
     * @return 小说列表
     */
    @GetMapping("/{categoryId}" + ApiPathConstants.LIST)
    public CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(
            @PathVariable("categoryId") Integer categoryId,
            PageReqDto pageReqDto
    ) {
        return bookService.listCategoryNovel(categoryId, pageReqDto);
    }

    /**
     * 查询小说详情接口
     *
     * @param bookId 小说id
     * @return BookInfoRespDto 小说详情实体
     */
    @GetMapping("/{bookId}")
    public CommonResult<BookInfoRespDto> getBookInfoById(@PathVariable("bookId") Long bookId) {
        return bookService.getBookInfoById(bookId);
    }
}
