package com.fengling.service;

import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;

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

    /**
     * 推荐类列表查询
     *
     * @param recommendReqDto 列表查询请求参数
     * @return 推荐类别列表
     */
    CommonResult<PageRespDto<AdminRecommendListRespDto>> listRecommendInfo(AdminRecommendReqDto recommendReqDto);

    /**
     * 修改推荐状态（上线/下线）
     *
     * @param recommendId 推荐id
     * @return 无
     */
    CommonResult<Void> updateRecommendInfo(Long recommendId, Integer recommendStatus);
}
