package com.fengling.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookShelfRespDto;

public interface BookShelfService {

    /**
     * 查询书架小说列表
     *
     * @param userId     用户id
     * @param pageReqDto 分页对象
     * @return 小说列表（分页后）
     */
    CommonResult<PageRespDto<BookShelfRespDto>> listShelfNovels(Long userId, PageReqDto pageReqDto);

    /**
     * 添加小说到书籍
     *
     * @param userId 用户id
     * @param bookId 书籍id
     * @return 无
     */
    CommonResult<Void> saveBookToBookShelf(Long userId, Long bookId);
}
