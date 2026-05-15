package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengling.entity.AuthorInfo;
import com.fengling.entity.dto.AuthorHomeRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthorMapper extends BaseMapper<AuthorInfo> {
    AuthorHomeRespDto getAuthorHomeInfo(@Param("userId") Long userId);
}
