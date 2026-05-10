package com.fengling.controller.front;

import com.fengling.common.constant.ApiPathConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.dto.BookCategoryRespDto;
import com.fengling.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiPathConstants.NOVEL_CATEGORY)
@RequiredArgsConstructor
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;

    /**
     * 首页小说分类接口
     * @return 小说分类列表
     */
    @GetMapping("/list")
    public CommonResult<List<BookCategoryRespDto>> listCategory(){
        return bookCategoryService.listCategory();
    }
}
