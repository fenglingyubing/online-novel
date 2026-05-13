package com.fengling.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookShelfListReq;
import com.fengling.entity.dto.BookShelfRespDto;

public interface BookShelfService {

    /**
     * 查询书架小说列表
     *
     * @param pageReqDto 分页对象
     * @return 小说列表（分页后）
     */
    CommonResult<PageRespDto<BookShelfRespDto>> listShelfNovels(PageReqDto pageReqDto);

    /**
     * 添加小说到书籍
     *
     * @param bookId 书籍id
     * @return 无
     */
    CommonResult<Void> saveBookToBookShelf(Long bookId);

    /**
     * 从书架删除小说
     * @param bookShelfListReq 小说id实体
     * @return 无
     */
    CommonResult<Void> deleteBookById(BookShelfListReq bookShelfListReq);
}
