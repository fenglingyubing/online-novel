package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookListRespDto;

import java.util.List;

public interface BookService {
    CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(Integer categoryId, PageReqDto pageReqDto);
}
