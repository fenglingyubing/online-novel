package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.AnnouncementInfo;
import com.fengling.entity.dto.AdminAnnouncementListRespDto;
import com.fengling.entity.dto.AdminAnnouncementRespDto;
import com.fengling.entity.dto.AnnouncementRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnnouncementInfoMapper extends BaseMapper<AnnouncementInfo> {

    /**
     * 公告列表查询
     *
     * @param page 分页请求参数
     * @return 公告列表
     */
    Page<AdminAnnouncementListRespDto> listAnnouncement(Page<AdminAnnouncementListRespDto> page);

    /**
     * 公告详情查询
     *
     * @param announcementId 公告id
     * @return 公告详情
     */
    AdminAnnouncementRespDto getAnnouncement(@Param("announcementId") Long announcementId);

    /**
     * 作家公告列表查询
     *
     * @param page 分页参数
     * @return 公告列表
     */
    Page<AnnouncementRespDto> listAnnouncementAuthor(Page<AnnouncementRespDto> page);

    /**
     * 用户公告列表查询
     *
     * @param page 分页请求参数
     * @return 用户公告列表
     */
    Page<AnnouncementRespDto> listAnnouncementUser(Page<AnnouncementRespDto> page);
}
