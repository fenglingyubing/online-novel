package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;

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

    /**
     * 修改变更信息状态
     *
     * @param auditId         审核id
     * @param auditInfoReqDto 审核信息请求实体
     * @return 无
     */
    CommonResult<Void> updateAdminAuditStatus(Long auditId, AdminAuditInfoReqDto auditInfoReqDto);

    /**
     * 修改新建作品状态
     *
     * @param auditId              审核id
     * @param adminAuditInfoReqDto 审核信息请求参数
     * @return 无
     */
    CommonResult<Void> updateAdminAuditCreateStatus(Long auditId, AdminAuditInfoReqDto adminAuditInfoReqDto);

    /**
     * 变更信息审核信息详情查询
     *
     * @param auditId     审核id
     * @param authorId    作家id
     * @param bookId      小说id
     * @param auditStatus 审核状态
     * @return 审核信息详情
     */
    CommonResult<AdminAuditInfoRespDto> getAuditInfo(Long auditId, Long authorId, Long bookId, Integer auditStatus);

    /**
     * 新书审核信息详情查询
     *
     * @param auditId     审核id
     * @param auditStatus
     * @return 新书审核信息详情
     */
    CommonResult<AdminAuditCreateRespDto> getAuditCreateInfo(Long auditId, Integer auditStatus);
}
