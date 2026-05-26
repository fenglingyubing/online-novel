package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.AdminRecommendCreateReqDto;
import com.fengling.entity.dto.AdminRecommendSearchReqDto;
import com.fengling.entity.dto.AdminRecommendSearchRespDto;

import java.util.List;

public interface AdminRecommendService {

    /**
     * 新增推荐
     *
     * @param createReqDto 推荐信息请求参数
     * @return 无
     */
    CommonResult<Void> saveAdminRecommendInfo(AdminRecommendCreateReqDto createReqDto);

    /**
     * 根据小说名或作家名获取小说信息
     *
     * @param searchReqDto 搜索请求参数
     * @return 小说信息
     */
    CommonResult<List<AdminRecommendSearchRespDto>> getSearchBookInfo(AdminRecommendSearchReqDto searchReqDto);
}
