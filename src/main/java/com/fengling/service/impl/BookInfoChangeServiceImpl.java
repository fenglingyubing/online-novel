package com.fengling.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AdminAuthUtil;
import com.fengling.common.util.PageAuthUtil;
import com.fengling.entity.dto.AdminAuditChaptersListRespDto;
import com.fengling.entity.dto.AdminAuditCreateListRespDto;
import com.fengling.entity.dto.AdminAuditListRespDto;
import com.fengling.mapper.BookInfoChangeMapper;
import com.fengling.service.BookInfoChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookInfoChangeServiceImpl implements BookInfoChangeService {

    private final AdminAuthUtil adminAuthUtil;
    private final BookInfoChangeMapper bookInfoChangeMapper;
    private final PageAuthUtil pageAuthUtil;

    @Override
    public CommonResult<PageRespDto<AdminAuditListRespDto>> listAdminAuditList(PageReqDto pageReqDto, Integer auditStatus) {
        argsAuth(pageReqDto, auditStatus);
        Page<AdminAuditListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminAuditListRespDto> pageAuditList = bookInfoChangeMapper.listAdminAuditList(page, auditStatus);
        return CommonResult.success(PageRespDto.of(pageAuditList));
    }

    @Override
    public CommonResult<PageRespDto<AdminAuditCreateListRespDto>> listAdminAuditCreateList(PageReqDto pageReqDto, Integer auditStatus) {
        argsAuth(pageReqDto, auditStatus);
        Page<AdminAuditCreateListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminAuditCreateListRespDto> pageAuditCreateList = bookInfoChangeMapper.listAdminAuditCreateList(
                page,
                auditStatus
        );
        return CommonResult.success(PageRespDto.of(pageAuditCreateList));
    }

    @Override
    public CommonResult<PageRespDto<AdminAuditChaptersListRespDto>> listAdminAuditChaptersList(PageReqDto pageReqDto, Integer auditStatus) {
        argsAuth(pageReqDto, auditStatus);
        Page<AdminAuditChaptersListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminAuditChaptersListRespDto> pageAuditChapters = bookInfoChangeMapper.listAdminAuditChaptersList(
                page,
                auditStatus
        );
        return CommonResult.success(PageRespDto.of(pageAuditChapters));
    }

    /**
     * 审核列表参数校验
     *
     * @param pageReqDto  分页请求参数
     * @param auditStatus 审核状态
     */
    private void argsAuth(PageReqDto pageReqDto, Integer auditStatus) {
        if (
                auditStatus == null ||
                        auditStatus < CommonConstants.CHAPTER_AUDIT_STATUS_AUDIT ||
                        auditStatus > CommonConstants.CHAPTER_AUDIT_STATUS_REJECTED
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        adminAuthUtil.adminAuth();
        pageAuthUtil.pageAuth(pageReqDto);
    }
}
