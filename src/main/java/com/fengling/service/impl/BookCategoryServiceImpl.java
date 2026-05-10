package com.fengling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.resp.CommonResult;
import com.fengling.entity.BookCategory;
import com.fengling.entity.dto.BookCategoryRespDto;
import com.fengling.mapper.BookCategoryMapper;
import com.fengling.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {
    private final BookCategoryMapper bookCategoryMapper;

    @Override
    public CommonResult<List<BookCategoryRespDto>> listCategory() {
        List<BookCategory> bookCategories = bookCategoryMapper.selectList(
                new LambdaQueryWrapper<BookCategory>()
                        .eq(BookCategory::getStatus, 1)
        );
        List<BookCategoryRespDto> bookCategoryRespDtoList = new ArrayList<>();
        for (BookCategory bookCategory : bookCategories) {
            bookCategoryRespDtoList.add(new BookCategoryRespDto(
                    bookCategory.getId(),
                    bookCategory.getCategoryName()
            ));
        }
        return CommonResult.success(bookCategoryRespDtoList);
    }
}
