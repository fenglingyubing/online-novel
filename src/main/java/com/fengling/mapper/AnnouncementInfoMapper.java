package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.AnnouncementInfo;
import com.fengling.entity.dto.AdminAnnouncementListRespDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementInfoMapper extends BaseMapper<AnnouncementInfo> {

    /**
     * 公告列表查询
     *
     * @param page 分页请求参数
     * @return 公告列表
     */
    Page<AdminAnnouncementListRespDto> listAnnouncement(Page<AdminAnnouncementListRespDto> page);
}
