package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ReadingHistoryReqDto;
import com.fengling.service.ReadingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.READING_HISTORY)
@RequiredArgsConstructor
public class ReadingHistoryController {

    private final ReadingHistoryService readingHistoryService;

    /**
     * 阅读历史更新
     *
     * @param bookId 小说id
     * @param reqDto 阅读历史更新请求参数
     * @return 无
     */
    @PutMapping("/{bookId}")
    public CommonResult<Void> updateReadingHistory(
            @PathVariable("bookId") Long bookId,
            @RequestBody ReadingHistoryReqDto reqDto
    ) {
        return readingHistoryService.updateReadingHistory(bookId, reqDto);
    }
}
