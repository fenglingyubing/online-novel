package com.fengling.controller.author;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AuthorBookInfoRespDto;
import com.fengling.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPathConstants.AUTHOR)
@RequiredArgsConstructor
public class AuthorBookController {

    private final BookService bookService;

    /**
     * 作家下的某个小说详情查询
     *
     * @param bookId     小说id
     * @param pageReqDto 分页参数
     * @return 小说详情
     */
    @GetMapping("/{bookId}")
    public CommonResult<AuthorBookInfoRespDto> getBookInfo(
            @PathVariable("bookId") Long bookId,
            PageReqDto pageReqDto
    ) {
        return bookService.getAuthorBookInfo(bookId, pageReqDto);
    }
}
