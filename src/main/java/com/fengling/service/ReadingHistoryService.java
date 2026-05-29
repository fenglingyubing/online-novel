package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ReadingHistoryReqDto;

public interface ReadingHistoryService {

    /**
     * 阅读历史更新
     *
     * @param bookId 小说id
     * @param reqDto 阅读历史更新请求参数
     * @return 无
     */
    CommonResult<Void> updateReadingHistory(Long bookId, ReadingHistoryReqDto reqDto);
}
