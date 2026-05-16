package com.fengling.controller.author;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ChapterEditInfoRespDto;
import com.fengling.entity.dto.ChapterUpdateReqDto;
import com.fengling.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPathConstants.AUTHOR)
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    /**
     * 作家章节编辑信息查询
     *
     * @param chapterId 章节id
     * @param bookId    小说id
     * @return 章节信息
     */
    @GetMapping("/{bookId}" + ApiPathConstants.CHAPTERS + "/{chapterId}")
    public CommonResult<ChapterEditInfoRespDto> getChapterInfo(
            @PathVariable("chapterId") Long chapterId,
            @PathVariable("bookId") Long bookId
    ) {
        return chapterService.getChapterInfo(bookId, chapterId);
    }

    @PutMapping("/{bookId}" + ApiPathConstants.CHAPTERS + "/{chapterId}")
    public CommonResult<Void> updateChapterInfo(
            @PathVariable("bookId") Long bookId,
            @PathVariable("chapterId") Long chapterId,
            @RequestBody ChapterUpdateReqDto chapterUpdateReqDto
    ) {
        return chapterService.updateChapterInfo(bookId, chapterId, chapterUpdateReqDto);
    }
}
