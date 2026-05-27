package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminAnnouncementCreateReqDto;
import com.fengling.service.AdminAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
