package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AdminAuthUtil;
import com.fengling.common.util.PageAuthUtil;
import com.fengling.entity.AnnouncementInfo;
import com.fengling.entity.dto.*;
import com.fengling.mapper.AnnouncementInfoMapper;
import com.fengling.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementInfoMapper announcementInfoMapper;
    private final AdminAuthUtil adminAuthUtil;
    private final PageAuthUtil pageAuthUtil;

    @Override
    public CommonResult<Void> saveAdminAnnouncementInfo(AdminAnnouncementCreateReqDto createReqDto) {
        if (createReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        AdminInfoDto adminInfoDto = adminAuthUtil.adminAuth();

        Integer announcementType = createReqDto.getAnnouncementType();
        Integer publishStatus = createReqDto.getPublishStatus();
        String title = createReqDto.getTitle();
        String content = createReqDto.getContent();
        if (
                title == null ||
                        title.isBlank() ||
                        announcementType == null ||
                        content == null ||
                        content.isBlank() ||
                        publishStatus == null
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        if (
                announcementType < CommonConstants.ANNOUNCEMENT_TYPE_ALL_USER ||
                        announcementType > CommonConstants.ANNOUNCEMENT_TYPE_READER ||
                        publishStatus < CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE ||
                        publishStatus > CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_UNDER
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AnnouncementInfo announcementInfo = BeanUtil.copyProperties(
                createReqDto,
                AnnouncementInfo.class
        );

        LocalDateTime now = LocalDateTime.now();
        announcementInfo.setPublisherId(adminInfoDto.getId());
        announcementInfo.setCreateTime(now);
        announcementInfo.setUpdateTime(now);

        if (CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE.equals(publishStatus)) {
            announcementInfo.setPublishTime(now);
        }

        int insert = announcementInfoMapper.insert(announcementInfo);

        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "创建公告失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<PageRespDto<AdminAnnouncementListRespDto>> listAnnouncement(PageReqDto pageReqDto) {
        adminAuthUtil.adminAuth();
        pageAuthUtil.pageAuth(pageReqDto);

        Page<AdminAnnouncementListRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );

        Page<AdminAnnouncementListRespDto> pageAnnouncement = announcementInfoMapper.listAnnouncement(page);
        return CommonResult.success(PageRespDto.of(pageAnnouncement));
    }

    @Override
    public CommonResult<AdminAnnouncementRespDto> getAnnouncement(Long announcementId) {
        adminAuthUtil.adminAuth();
        if (announcementId < 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AdminAnnouncementRespDto announcementRespDto = announcementInfoMapper.getAnnouncement(announcementId);
        if (announcementRespDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND);
        }
        return CommonResult.success(announcementRespDto);
    }

    @Override
    public CommonResult<Void> updateAnnouncement(Long announcementId, AdminAnnouncementCreateReqDto updateReqDto) {
        adminAuthUtil.adminAuth();

        if (updateReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        String title = updateReqDto.getTitle();
        String content = updateReqDto.getContent();
        Integer announcementType = updateReqDto.getAnnouncementType();
        Integer publishStatus = updateReqDto.getPublishStatus();

        if (
                title == null &&
                        content == null &&
                        announcementType == null &&
                        publishStatus == null
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        if (
                (title != null && title.isBlank()) ||
                        (content != null && content.isBlank())
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        if (
                (announcementType != null &&
                        (announcementType < CommonConstants.ANNOUNCEMENT_TYPE_ALL_USER ||
                                announcementType > CommonConstants.ANNOUNCEMENT_TYPE_READER)) ||
                        (publishStatus != null &&
                                (publishStatus < CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE ||
                                        publishStatus > CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_UNDER))
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AnnouncementInfo selectOne = announcementInfoMapper.selectOne(
                new LambdaQueryWrapper<AnnouncementInfo>()
                        .select(AnnouncementInfo::getId, AnnouncementInfo::getPublishStatus)
                        .eq(AnnouncementInfo::getId, announcementId)
        );

        if (selectOne == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "公告信息不存在");
        }

        AnnouncementInfo announcementInfo = BeanUtil.copyProperties(
                updateReqDto,
                AnnouncementInfo.class
        );
        announcementInfo.setId(announcementId);
        LocalDateTime now = LocalDateTime.now();
        announcementInfo.setUpdateTime(now);
        if (
                CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE.equals(publishStatus) &&
                        !CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE.equals(selectOne.getPublishStatus())
        ) {
            announcementInfo.setPublishTime(now);
        }

        int i = announcementInfoMapper.updateById(announcementInfo);

        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "修改失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<Void> updateAnnouncementStatus(Long announcementId, Integer publishStatus) {
        adminAuthUtil.adminAuth();

        if (
                publishStatus == null ||
                        publishStatus < CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE ||
                        publishStatus > CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_UNDER
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AnnouncementInfo selectOne = announcementInfoMapper.selectOne(
                new LambdaQueryWrapper<AnnouncementInfo>()
                        .select(AnnouncementInfo::getId, AnnouncementInfo::getPublishStatus)
                        .eq(AnnouncementInfo::getId, announcementId)
        );

        if (selectOne == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "公告信息不存在");
        }

        AnnouncementInfo announcementInfo = new AnnouncementInfo();
        announcementInfo.setId(announcementId);
        announcementInfo.setPublishStatus(publishStatus);
        LocalDateTime now = LocalDateTime.now();
        announcementInfo.setUpdateTime(now);
        if (
                CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE.equals(publishStatus) &&
                        !CommonConstants.ANNOUNCEMENT_PUBLISH_STATUS_RELEASE.equals(selectOne.getPublishStatus())
        ) {
            announcementInfo.setPublishTime(now);
        }

        int update = announcementInfoMapper.updateById(announcementInfo);

        if (update != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "更改失败");
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<PageRespDto<AnnouncementRespDto>> listAnnouncementUser(PageReqDto pageReqDto) {
        pageAuthUtil.pageAuth(pageReqDto);
        Page<AnnouncementRespDto> page = new Page<>(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize()
        );
        Page<AnnouncementRespDto> pageAnnouncement = announcementInfoMapper.listAnnouncementUser(page);
        return CommonResult.success(PageRespDto.of(pageAnnouncement));
    }

    @Override
    public CommonResult<AnnouncementInfoRespDto> getAnnouncementInfo(Long announcementId) {
        if (announcementId <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        AnnouncementInfoRespDto respDto = announcementInfoMapper.getAnnouncementInfo(announcementId);
        if (respDto == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "公告信息不存在");
        }
        return CommonResult.success(respDto);
    }
}
