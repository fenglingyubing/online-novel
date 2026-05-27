package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminAnnouncementCreateReqDto;

public interface AdminAnnouncementService {

    /**
     * 创建公告
     *
     * @param createReqDto 创建公告请求参数
     * @return 无
     */
    CommonResult<Void> saveAdminAnnouncementInfo(AdminAnnouncementCreateReqDto createReqDto);
}
