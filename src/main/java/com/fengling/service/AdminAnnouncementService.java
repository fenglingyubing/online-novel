package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminAnnouncementCreateReqDto;
import com.fengling.entity.dto.AdminAnnouncementListRespDto;
import com.fengling.entity.dto.AdminAnnouncementRespDto;

public interface AdminAnnouncementService {

    /**
     * 创建公告
     *
     * @param createReqDto 创建公告请求参数
     * @return 无
     */
    CommonResult<Void> saveAdminAnnouncementInfo(AdminAnnouncementCreateReqDto createReqDto);

    /**
     * 公告列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 公告列表
     */
    CommonResult<PageRespDto<AdminAnnouncementListRespDto>> listAnnouncement(PageReqDto pageReqDto);

    /**
     * 公告详情查询
     *
     * @param announcementId 公告id
     * @return 公告详情
     */
    CommonResult<AdminAnnouncementRespDto> getAnnouncement(Long announcementId);

    /**
     * 公告信息修改
     *
     * @param announcementId 公告id
     * @param updateReqDto   变更信息请求参数
     * @return 无
     */
    CommonResult<Void> updateAnnouncement(Long announcementId, AdminAnnouncementCreateReqDto updateReqDto);
}
