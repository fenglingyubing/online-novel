package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminAuditInfoReqDto;

public interface ChapterAuditService {

    /**
     * 章节审核状态更新
     *
     * @param auditId         审核Id
     * @param auditInfoReqDto 审核请求参数
     * @return 无
     */
    CommonResult<Void> updateAdminAuditChaptersStatus(Long auditId, AdminAuditInfoReqDto auditInfoReqDto);
}
