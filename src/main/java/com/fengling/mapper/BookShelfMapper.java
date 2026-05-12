package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.BookShelf;
import com.fengling.entity.dto.BookShelfRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookShelfMapper extends BaseMapper<BookShelf> {
    Page<BookShelfRespDto> listShelfNovels(Page<BookShelfRespDto> page, @Param("userId") Long userId);
}
