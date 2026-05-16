package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.ChapterEditInfoRespDto;

public interface ChapterService {


    CommonResult<ChapterEditInfoRespDto> getChapterInfo(Long bookId, Long chapterId);
}
