package com.fengling.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AdminAuthUtil;
import com.fengling.common.util.PageAuthUtil;
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
        adminAuthUtil.adminAuth();
        pageAuthUtil.pageAuth(pageReqDto);
        Page<AdminAuditListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AdminAuditListRespDto> pageAuditList = bookInfoChangeMapper.listAdminAuditList(page, auditStatus);
        return CommonResult.success(PageRespDto.of(pageAuditList));
    }
}
