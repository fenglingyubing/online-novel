package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengling.entity.AuthorInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthorMapper extends BaseMapper<AuthorInfo> {
}
