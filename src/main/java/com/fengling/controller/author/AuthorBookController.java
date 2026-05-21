package com.fengling.controller.author;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;
import com.fengling.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 小说变更信息提交审核
     *
     * @param bookId         小说id
     * @param bookInfoReqDto 小说变更请求参数
     * @return 无
     */
    @PostMapping("/{bookId}")
    public CommonResult<Void> saveChangeBookInfo(
            @PathVariable("bookId") Long bookId,
            @RequestBody AuthorBookInfoReqDto bookInfoReqDto
    ) {
        return bookService.saveChangeBookInfo(bookId, bookInfoReqDto);
    }

    /**
     * 小说封面变更
     *
     * @param file     上传文件
     * @param coverUrl 封面链接
     * @return 无
     */
    @PostMapping("/{bookId}/uploadcover")
    public CommonResult<Void> saveBookCoverUrl(
            @PathVariable("bookId") Long bookId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "coverUrl", required = false) String coverUrl
    ) {
        return bookService.saveBookCoverUrl(bookId, file, coverUrl);
    }

    /**
     * 查询小说变更信息审核列表
     *
     * @return 审核信息列表
     */
    @GetMapping("/bookinfo/audit")
    public CommonResult<PageRespDto<AuthorBookInfoAuditRespDto>> listBookInfoAudits(PageReqDto pageReqDto) {
        return bookService.listBookInfoAudits(pageReqDto);
    }

    /**
     * 作家更新小说信息-无需审核
     *
     * @param bookInfoNotAuditReqDto 更新信息参数
     * @return 无
     */
    @PutMapping("/{bookId}")
    public CommonResult<Void> updateAuthorBookInfo(
            @PathVariable("bookId") Long bookId,
            @RequestBody AuthorBookInfoNotAuditReqDto bookInfoNotAuditReqDto
    ) {
        return bookService.updateAuthorBookInfo(bookId, bookInfoNotAuditReqDto);
    }

    /**
     * 作家新建作品
     *
     * @param createBookReqDto 新建作品请求参数
     * @param file             上传的图片文件
     * @param coverUrl         图片链接
     * @return 无
     */
    @PostMapping("/create")
    public CommonResult<Void> saveCreateBookInfo(
            @ModelAttribute AuthorCreateBookReqDto createBookReqDto,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "coverUrl", required = false) String coverUrl
    ) {
        return bookService.saveCreateBookInfo(createBookReqDto, file, coverUrl);
    }

    /**
     * 删除信息变更
     *
     * @param auditId 审核信息id
     * @return 无
     */
    @DeleteMapping("/bookinfo/audit/{auditId}")
    public CommonResult<Void> deleteAuditInfo(@PathVariable("auditId") Long auditId){
        return bookService.deleteAuditInfo(auditId);
    }

    /**
     * 变更信息详情查看
     *
     * @param auditId 变更信息审核id
     * @return 变更信息详情
     */
    @GetMapping("/bookinfo/audit/{auditId}")
    public CommonResult<AuthorAuditInfoRespDto> getAuditInfo(@PathVariable("auditId") Long auditId){
        return bookService.getAuditInfo(auditId);
    }
}
