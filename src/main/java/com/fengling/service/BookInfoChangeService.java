package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminAuditChaptersListRespDto;
import com.fengling.entity.dto.AdminAuditCreateListRespDto;
import com.fengling.entity.dto.AdminAuditListRespDto;

public interface BookInfoChangeService {

    /**
     * 变更信息审核列表查询
     *
     * @param pageReqDto  分页参数
     * @param auditStatus 审核状态
     * @return
     */
    CommonResult<PageRespDto<AdminAuditListRespDto>> listAdminAuditList(PageReqDto pageReqDto, Integer auditStatus);

    /**
     * 新书审核列表查询
     *
     * @param pageReqDto  分页请求参数
     * @param auditStatus 审核状态
     * @return 新书审核列表
     */
    CommonResult<PageRespDto<AdminAuditCreateListRespDto>> listAdminAuditCreateList(PageReqDto pageReqDto,
                                                                                    Integer auditStatus);

    /**
     * 章节审核列表查询
     *
     * @param pageReqDto  分页请求参数
     * @param auditStatus 审核状态
     * @return 章节审核列表
     */
    CommonResult<PageRespDto<AdminAuditChaptersListRespDto>> listAdminAuditChaptersList(PageReqDto pageReqDto,
                                                                                        Integer auditStatus);
}
