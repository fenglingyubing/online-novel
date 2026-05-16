package com.fengling.controller.author;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ChapterEditInfoRespDto;
import com.fengling.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.AUTHOR)
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping("/{bookId}" + ApiPathConstants.CHAPTERS + "/{chapterId}")
    public CommonResult<ChapterEditInfoRespDto> getChapterInfo(
            @PathVariable("chapterId") Long chapterId,
            @PathVariable("bookId") Long bookId
    ) {
        return chapterService.getChapterInfo(bookId, chapterId);
    }
}
