package com.fengling.service;

import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookCategoryRespDto;

import java.util.List;

public interface BookCategoryService {
    /**
     * 首页小说分类接口
     * @return 小说分类列表
     */
    CommonResult<List<BookCategoryRespDto>> listCategory();
}
