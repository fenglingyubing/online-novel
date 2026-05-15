package com.fengling.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengling.common.constant.CacheConstants;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.RedisUtil;
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
    private final RedisUtil redisUtil;

    @Override
    public CommonResult<List<BookCategoryRespDto>> listCategory() {
        // 从redis获取
        String jsonStr = redisUtil.getValueForKey(CacheConstants.CATEGORY);
        if (jsonStr != null && !jsonStr.isBlank()) {
            return CommonResult.success(JSONUtil.toList(jsonStr, BookCategoryRespDto.class));
        }
        List<BookCategory> bookCategories = bookCategoryMapper.selectList(
                new LambdaQueryWrapper<BookCategory>()
                        .select(BookCategory::getId, BookCategory::getCategoryName)
                        .eq(BookCategory::getStatus, 1)
                        .orderByAsc(BookCategory::getId)
        );
        List<BookCategoryRespDto> bookCategoryRespDtoList = new ArrayList<>();
        for (BookCategory bookCategory : bookCategories) {
            bookCategoryRespDtoList.add(new BookCategoryRespDto(
                    bookCategory.getId(),
                    bookCategory.getCategoryName()
            ));
        }
        // 将数据转字符串
        String valueStr = JSONUtil.toJsonStr(bookCategoryRespDtoList);
        // 将数据缓存到redis
        redisUtil.addRedisCache(
                CacheConstants.CATEGORY,
                valueStr,
                CacheConstants.CATEGORY_TTL
        );
        return CommonResult.success(bookCategoryRespDtoList);
    }
}
