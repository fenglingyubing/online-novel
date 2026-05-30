package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ReadingHistoryReqDto;
import com.fengling.entity.dto.ReadingHistoryRespDto;

public interface ReadingHistoryService {

    /**
     * 阅读历史更新
     *
     * @param bookId 小说id
     * @param reqDto 阅读历史更新请求参数
     * @return 无
     */
    CommonResult<Void> updateReadingHistory(Long bookId, ReadingHistoryReqDto reqDto);

    /**
     * 阅读历史列表查询
     *
     * @param pageReqDto 分页请求参数
     * @return 阅读历史列表
     */
    CommonResult<PageRespDto<ReadingHistoryRespDto>> listReadingHistory(PageReqDto pageReqDto);
}
