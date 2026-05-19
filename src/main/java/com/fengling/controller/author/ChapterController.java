package com.fengling.controller.author;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.*;
import com.fengling.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 更新章节信息
     *
     * @param bookId              小说id
     * @param chapterId           章节id
     * @param chapterUpdateReqDto 章节更新请求参数
     * @return 无
     */
    @PutMapping("/{bookId}" + ApiPathConstants.CHAPTERS + "/{chapterId}")
    public CommonResult<Void> updateChapterInfo(
            @PathVariable("bookId") Long bookId,
            @PathVariable("chapterId") Long chapterId,
            @RequestBody ChapterUpdateReqDto chapterUpdateReqDto
    ) {
        return chapterService.updateChapterInfo(bookId, chapterId, chapterUpdateReqDto);
    }

    /**
     * 新增章节
     *
     * @param bookId            小说id
     * @param chapterSaveReqDto 新增章节参数
     * @return 章节id
     */
    @PostMapping("/{bookId}" + ApiPathConstants.CHAPTERS)
    public CommonResult<ChapterSaveRespDto> saveChapterInfo(
            @PathVariable("bookId") Long bookId,
            @RequestBody ChapterSaveReqDto chapterSaveReqDto
    ) {
        return chapterService.saveChapterInfo(bookId, chapterSaveReqDto);
    }

    /**
     * 删除章节草稿
     *
     * @param bookId        小说id
     * @param chapterIdList 章节id列表
     * @return 无
     */
    @DeleteMapping("/{bookId}/chapters")
    public CommonResult<Void> deleteChapters(
            @PathVariable("bookId") Long bookId,
            @RequestBody List<Long> chapterIdList
    ) {
        return chapterService.deleteChapters(bookId, chapterIdList);
    }

    /**
     * 审核章节撤回
     *
     * @param bookId    小说id
     * @param chapterId 章节id
     * @return 无
     */
    @PutMapping("/{bookId}" + ApiPathConstants.CHAPTERS + "/{chapterId}" + "/cancel")
    public CommonResult<Void> updateChapterStatus(
            @PathVariable("bookId") Long bookId,
            @PathVariable("chapterId") Long chapterId
    ) {
        return chapterService.updateChapterStatus(bookId, chapterId);
    }
}
