package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;
import com.fengling.service.BookInfoChangeService;
import com.fengling.service.ChapterAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.ADMIN)
@RequiredArgsConstructor
public class AdminAuditController {

    private final BookInfoChangeService bookInfoChangeService;
    private final ChapterAuditService chapterAuditService;

    /**
     * 管理后台-查询变更信息审核列表
     *
     * @param pageReqDto 分页请求参数
     * @return 审核信息列表
     */
    @GetMapping(ApiPathConstants.LIST)
    public CommonResult<PageRespDto<AdminAuditListRespDto>> listAdminAuditList(
            PageReqDto pageReqDto,
            @RequestParam("auditStatus") Integer auditStatus
    ) {
        return bookInfoChangeService.listAdminAuditList(pageReqDto, auditStatus);
    }

    /**
     * 新书审核列表查询
     *
     * @param pageReqDto  分页请求参数
     * @param auditStatus 审核状态
     * @return 新书审核列表
     */
    @GetMapping(ApiPathConstants.LIST + "/create")
    public CommonResult<PageRespDto<AdminAuditCreateListRespDto>> listAdminAuditCreateList(
            PageReqDto pageReqDto,
            @RequestParam("auditStatus") Integer auditStatus
    ) {
        return bookInfoChangeService.listAdminAuditCreateList(pageReqDto, auditStatus);
    }

    /**
     * 章节审核列表查询
     *
     * @param pageReqDto  分页请求参数
     * @param auditStatus 审核状态
     * @return 章节审核列表
     */
    @GetMapping(ApiPathConstants.LIST + ApiPathConstants.CHAPTERS)
    public CommonResult<PageRespDto<AdminAuditChaptersListRespDto>> listAdminAuditChaptersList(
            PageReqDto pageReqDto,
            @RequestParam("auditStatus") Integer auditStatus
    ) {
        return bookInfoChangeService.listAdminAuditChaptersList(pageReqDto, auditStatus);
    }

    /**
     * 修改变更信息状态
     *
     * @param auditId 审核id
     * @return 无
     */
    @PutMapping(ApiPathConstants.LIST + "/audit/{auditId}")
    public CommonResult<Void> updateAdminAuditStatus(
            @PathVariable("auditId") Long auditId,
            @RequestBody AdminAuditInfoReqDto adminAuditInfoReqDto
    ) {
        return bookInfoChangeService.updateAdminAuditStatus(auditId, adminAuditInfoReqDto);
    }

    /**
     * 修改新建作品状态
     *
     * @param auditId              审核id
     * @param adminAuditInfoReqDto 审核信息请求参数
     * @return 无
     */
    @PutMapping(ApiPathConstants.LIST + "/audit/{auditId}/create")
    public CommonResult<Void> updateAdminAuditCreateStatus(
            @PathVariable("auditId") Long auditId,
            @RequestBody AdminAuditInfoReqDto adminAuditInfoReqDto
    ) {
        return bookInfoChangeService.updateAdminAuditCreateStatus(auditId, adminAuditInfoReqDto);
    }

    /**
     * 章节审核状态更新
     *
     * @param auditId         审核Id
     * @param auditInfoReqDto 审核请求参数
     * @return 无
     */
    @PutMapping(ApiPathConstants.LIST + "/audit/{auditId}" + ApiPathConstants.CHAPTERS)
    public CommonResult<Void> updateAdminAuditChaptersStatus(
            @PathVariable("auditId") Long auditId,
            @RequestBody AdminAuditInfoReqDto auditInfoReqDto
    ) {
        return chapterAuditService.updateAdminAuditChaptersStatus(auditId, auditInfoReqDto);
    }

    /**
     * 变更信息审核信息详情查询
     *
     * @param auditId     审核id
     * @param auditStatus 审核状态
     * @return 审核信息详情
     */
    @GetMapping("/{auditId}/info/{authorId}/{bookId}")
    public CommonResult<AdminAuditInfoRespDto> getAuditInfo(
            @PathVariable("auditId") Long auditId,
            @PathVariable("authorId") Long authorId,
            @PathVariable("bookId") Long bookId,
            @RequestParam("auditStatus") Integer auditStatus
    ) {
        return bookInfoChangeService.getAuditInfo(auditId, authorId, bookId, auditStatus);
    }

    /**
     * 新书审核信息详情查询
     *
     * @param auditId 审核id
     * @return 新书审核信息详情
     */
    @GetMapping("/{auditId}/info/create")
    public CommonResult<AdminAuditCreateRespDto> getAuditCreateInfo(
            @PathVariable("auditId") Long auditId,
            @RequestParam("auditStatus") Integer auditStatus
    ) {
        return bookInfoChangeService.getAuditCreateInfo(auditId, auditStatus);
    }

    /**
     * 章节审核信息详情查询
     *
     * @param auditId     审核id
     * @param auditStatus 审核状态
     * @return 章节审核信息详情
     */
    @GetMapping("/{auditId}/info/chapter")
    public CommonResult<AdminAuditChapterRespDto> getAuditChapterInfo(
            @PathVariable("auditId") Long auditId,
            @RequestParam("auditStatus") Integer auditStatus
    ) {
        return chapterAuditService.getAuditChapterInfo(auditId, auditStatus);
    }
}
