package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminAnnouncementCreateReqDto;
import com.fengling.entity.dto.AdminAnnouncementListRespDto;
import com.fengling.service.AdminAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.ANNOUNCEMENT)
@RequiredArgsConstructor
public class AdminAnnouncementController {

    private final AdminAnnouncementService announcementService;

    /**
     * 创建公告
     *
     * @param createReqDto 创建公告请求参数
     * @return 无
     */
    @PostMapping("/create")
    public CommonResult<Void> saveAdminAnnouncementInfo(@RequestBody AdminAnnouncementCreateReqDto createReqDto) {
        return announcementService.saveAdminAnnouncementInfo(createReqDto);
    }

    /**
     * 公告列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 公告列表
     */
    @GetMapping(ApiPathConstants.LIST)
    public CommonResult<PageRespDto<AdminAnnouncementListRespDto>> listAnnouncement(PageReqDto pageReqDto) {
        return announcementService.listAnnouncement(pageReqDto);
    }
}
