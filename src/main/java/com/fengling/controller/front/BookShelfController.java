package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookShelfListReqDto;
import com.fengling.entity.dto.BookShelfRespDto;
import com.fengling.service.BookShelfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiPathConstants.SHELF)
@RequiredArgsConstructor
public class BookShelfController {

    private final BookShelfService bookShelfService;

    /**
     * 查询书架书籍列表
     *
     * @param pageReqDto 分页对象
     * @return 小说列表（分页后）
     */
    @GetMapping( ApiPathConstants.LIST)
    public CommonResult<PageRespDto<BookShelfRespDto>> listShelfNovels(PageReqDto pageReqDto) {
        return bookShelfService.listShelfNovels(pageReqDto);
    }

    /**
     * 添加小说到书架
     *
     * @param bookId 书籍id
     * @return 无
     */
    @PostMapping("/{bookId}")
    public CommonResult<Void> saveBookToBookShelf(@PathVariable("bookId") Long bookId) {
        return bookShelfService.saveBookToBookShelf(bookId);
    }

    /**
     * 从书架删除小说
     * @param bookShelfListReqDto 小说Id实体
     * @return 无
     */
    @DeleteMapping()
    public CommonResult<Void> deleteBookById(@RequestBody BookShelfListReqDto bookShelfListReqDto){
        return bookShelfService.deleteBookById(bookShelfListReqDto);
    }

}
