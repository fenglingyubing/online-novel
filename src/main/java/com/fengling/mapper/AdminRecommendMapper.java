package com.fengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.entity.AdminRecommend;
import com.fengling.entity.dto.AdminRecommendListRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminRecommendMapper extends BaseMapper<AdminRecommend> {

    /**
     * 推荐列表查询
     *
     * @param page          分页请求参数
     * @param recommendType 推荐类型
     * @return 推荐列表
     */
    Page<AdminRecommendListRespDto> listRecommendInfo(Page<AdminRecommendListRespDto> page,
                                                      @Param("recommendType") Integer recommendType);
}
