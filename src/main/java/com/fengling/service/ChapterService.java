package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ChapterEditInfoRespDto;
import com.fengling.entity.dto.ChapterUpdateReqDto;

public interface ChapterService {

    /**
     * 作家章节编辑信息查询
     *
     * @param bookId    小说id
     * @param chapterId 章节id
     * @return 章节信息
     */
    CommonResult<ChapterEditInfoRespDto> getChapterInfo(Long bookId, Long chapterId);

    CommonResult<Void> updateChapterInfo(Long bookId, Long chapterId, ChapterUpdateReqDto chapterUpdateReqDto);
}
