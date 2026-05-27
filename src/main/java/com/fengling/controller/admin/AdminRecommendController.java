package com.fengling.controller.admin;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;
import com.fengling.service.AdminRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPathConstants.RECOMMEND)
@RequiredArgsConstructor
public class AdminRecommendController {

    private final AdminRecommendService adminRecommendService;

    /**
     * 新增推荐
     *
     * @param createReqDto 推荐信息请求参数
     * @return 无
     */
    @PostMapping("/create")
    public CommonResult<Void> saveAdminRecommendInfo(@RequestBody AdminRecommendCreateReqDto createReqDto) {
        return adminRecommendService.saveAdminRecommendInfo(createReqDto);
    }

    /**
     * 根据小说名或作家名获取小说信息
     *
     * @param searchReqDto 搜索请求参数
     * @return 小说信息
     */
    @GetMapping("/create/search")
    public CommonResult<List<AdminRecommendSearchRespDto>> getSearchBookInfo(AdminRecommendSearchReqDto searchReqDto) {
        return adminRecommendService.getSearchBookInfo(searchReqDto);
    }

    /**
     * 推荐类列表查询
     *
     * @param recommendReqDto 列表查询请求参数
     * @return 推荐类别列表
     */
    @GetMapping(ApiPathConstants.LIST)
    public CommonResult<PageRespDto<AdminRecommendListRespDto>> listRecommendInfo(AdminRecommendReqDto recommendReqDto) {
        return adminRecommendService.listRecommendInfo(recommendReqDto);
    }

    /**
     * 修改推荐状态（上线/下线）
     *
     * @param recommendId 推荐id
     * @return 无
     */
    @PutMapping("/update/{recommendId}/{recommendStatus}")
    public CommonResult<Void> updateRecommendInfo(
            @PathVariable("recommendId") Long recommendId,
            @PathVariable("recommendStatus") Integer recommendStatus
    ) {
        return adminRecommendService.updateRecommendInfo(recommendId, recommendStatus);
    }
}
