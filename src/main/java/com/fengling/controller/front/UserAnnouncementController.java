package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AnnouncementInfoRespDto;
import com.fengling.entity.dto.AnnouncementRespDto;
import com.fengling.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPathConstants.HOME)
@RequiredArgsConstructor
public class UserAnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * 用户公告列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 用户公告列表
     */
    @GetMapping(ApiPathConstants.LIST + "/announcement")
    public CommonResult<PageRespDto<AnnouncementRespDto>> listAnnouncement(PageReqDto pageReqDto) {
        return announcementService.listAnnouncementUser(pageReqDto);
    }

    /**
     * 用户公告详情查询
     *
     * @param announcementId 公告id
     * @return 公告详情
     */
    @GetMapping(ApiPathConstants.LIST + "/announcement/{announcementId}")
    public CommonResult<AnnouncementInfoRespDto> getAnnouncementInfo(
            @PathVariable("announcementId") Long announcementId
    ) {
        return announcementService.getAnnouncementInfo(announcementId);
    }
}
