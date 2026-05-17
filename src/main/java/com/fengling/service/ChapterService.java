package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ChapterEditInfoRespDto;
import com.fengling.entity.dto.ChapterSaveReqDto;
import com.fengling.entity.dto.ChapterSaveRespDto;
import com.fengling.entity.dto.ChapterUpdateReqDto;

import java.util.List;

public interface ChapterService {

    /**
     * 作家章节编辑信息查询
     *
     * @param bookId    小说id
     * @param chapterId 章节id
     * @return 章节信息
     */
    CommonResult<ChapterEditInfoRespDto> getChapterInfo(Long bookId, Long chapterId);

    /**
     * 更新章节信息
     *
     * @param bookId              小说id
     * @param chapterId           章节id
     * @param chapterUpdateReqDto 章节更新请求参数
     * @return 无
     */
    CommonResult<Void> updateChapterInfo(Long bookId, Long chapterId, ChapterUpdateReqDto chapterUpdateReqDto);

    /**
     * 新增章节
     *
     * @param bookId      小说id
     * @param chapterInfo 章节请求参数
     * @return 新增章节id
     */
    CommonResult<ChapterSaveRespDto> saveChapterInfo(Long bookId, ChapterSaveReqDto chapterInfo);

    CommonResult<Void> deleteChapters(Long bookId, List<Long> chapterIdList);
}
