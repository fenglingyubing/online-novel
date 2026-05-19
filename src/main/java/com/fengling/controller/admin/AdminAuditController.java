package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
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
     * 管理后台-查询变更信息审核列
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
}
