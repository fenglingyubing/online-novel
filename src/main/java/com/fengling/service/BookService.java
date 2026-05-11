package com.fengling.service;

import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookInfoRespDto;
import com.fengling.entity.dto.BookListRespDto;

import java.util.List;

public interface BookService {
    /**
     * 查询分类下的小说（分页）
     *
     * @param categoryId 分类id
     * @param pageReqDto 分页实体
     * @return 小说列表
     */
    CommonResult<PageRespDto<BookListRespDto>> listCategoryNovel(Integer categoryId, PageReqDto pageReqDto);

    /**
     * 查询小说详情
     *
     * @param bookId 小说id
     * @return BookInfoRespDto
     */
    CommonResult<BookInfoRespDto> getBookInfoById(Long bookId);
}
