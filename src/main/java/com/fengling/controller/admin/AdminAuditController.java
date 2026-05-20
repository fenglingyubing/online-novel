package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminAuditChaptersListRespDto;
import com.fengling.entity.dto.AdminAuditCreateListRespDto;
import com.fengling.entity.dto.AdminAuditListRespDto;
import com.fengling.service.BookInfoChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPathConstants.ADMIN)
@RequiredArgsConstructor
public class AdminAuditController {

    private final BookInfoChangeService bookInfoChangeService;

    /**
     * 管理后台-查询变更信息审核列表
     *
     * @param pageReqDto 分页请求参数
     * @return 审核信息列表
     */
    @GetMapping("/list")
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
    @GetMapping("/list/create")
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
    @GetMapping("/list/chapters")
    public CommonResult<PageRespDto<AdminAuditChaptersListRespDto>> listAdminAuditChaptersList(
            PageReqDto pageReqDto,
            @RequestParam("auditStatus") Integer auditStatus
    ) {
        return bookInfoChangeService.listAdminAuditChaptersList(pageReqDto, auditStatus);
    }
}
