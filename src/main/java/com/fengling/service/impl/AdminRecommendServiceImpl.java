package com.fengling.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengling.common.constant.CommonConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.dto.PageRespDto;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.resp.CommonResult;
import com.fengling.common.util.AdminAuthUtil;
import com.fengling.entity.AdminRecommend;
import com.fengling.entity.BookInfo;
import com.fengling.entity.dto.*;
import com.fengling.mapper.AdminRecommendMapper;
import com.fengling.mapper.BookMapper;
import com.fengling.service.AdminRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminRecommendServiceImpl implements AdminRecommendService {

    private final AdminRecommendMapper adminRecommendMapper;
    private final AdminAuthUtil adminAuthUtil;
    private final BookMapper bookMapper;

    @Override
    public CommonResult<Void> saveAdminRecommendInfo(AdminRecommendCreateReqDto createReqDto) {
        if (createReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }
        AdminInfoDto adminInfoDto = adminAuthUtil.adminAuth();

        Integer recommendType = createReqDto.getRecommendType();
        LocalDateTime startTime = createReqDto.getStartTime();
        LocalDateTime endTime = createReqDto.getEndTime();
        Long bookId = createReqDto.getBookId();
        Integer categoryId = createReqDto.getCategoryId();

        // 判断是否有参数为空
        if (
                recommendType == null ||
                        startTime == null ||
                        endTime == null ||
                        bookId == null
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        if (
                !CommonConstants.RECOMMEND_TYPE_HOME.equals(recommendType) &&
                        !CommonConstants.RECOMMEND_TYPE_CATEGORY.equals(recommendType)
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        // 判断在推荐类型是分类页时分类id是否为空
        if (
                CommonConstants.RECOMMEND_TYPE_CATEGORY.equals(recommendType) &&
                        categoryId == null
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        // 判断起始时间是否在结束时间之后
        if (!startTime.isBefore(endTime)) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        BookInfo bookInfo = bookMapper.selectOne(
                new LambdaQueryWrapper<BookInfo>()
                        .select(
                                BookInfo::getId,
                                BookInfo::getCategoryId
                        )
                        .eq(BookInfo::getId, bookId)
                        .eq(BookInfo::getPublishStatus, CommonConstants.PUBLISH_STATUS_SHELVES)
        );

        // 校验符合要求的书籍是否存在
        if (bookInfo == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "小说信息不存在");
        }

        if (
                CommonConstants.RECOMMEND_TYPE_CATEGORY.equals(recommendType) &&
                        !Objects.equals(bookInfo.getCategoryId(), categoryId)
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AdminRecommend adminRecommend = BeanUtil.copyProperties(
                createReqDto,
                AdminRecommend.class
        );
        adminRecommend.setAdminId(adminInfoDto.getId());
        int insert = adminRecommendMapper.insert(adminRecommend);

        if (insert != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL);
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult<List<AdminRecommendSearchRespDto>> getSearchBookInfo(AdminRecommendSearchReqDto searchReqDto) {
        adminAuthUtil.adminAuth();

        String bookName = searchReqDto.getBookName();
        String authorName = searchReqDto.getAuthorName();

        if (
                (bookName == null || bookName.isBlank()) &&
                        (authorName == null || authorName.isBlank())
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        List<AdminRecommendSearchRespDto> searchRespDto = bookMapper.getSearchBookInfo(
                bookName,
                authorName
        );
        return CommonResult.success(searchRespDto);
    }

    @Override
    public CommonResult<PageRespDto<AdminRecommendListRespDto>> listRecommendInfo(AdminRecommendReqDto recommendReqDto) {
        adminAuthUtil.adminAuth();

        if (recommendReqDto == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Long pageNum = recommendReqDto.getPageNum();
        Long pageSize = recommendReqDto.getPageSize();
        if (pageNum == null || pageNum <= 0 || pageSize == null || pageSize <= 0 || pageSize > 20) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Integer recommendType = recommendReqDto.getRecommendType();
        if (
                recommendType != null &&
                        !CommonConstants.RECOMMEND_TYPE_HOME.equals(recommendType) &&
                        !CommonConstants.RECOMMEND_TYPE_CATEGORY.equals(recommendType)
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        Page<AdminRecommendListRespDto> page = new Page<>(pageNum, pageSize);
        Page<AdminRecommendListRespDto> pageRecommend = adminRecommendMapper.listRecommendInfo(
                page,
                recommendType
        );
        return CommonResult.success(PageRespDto.of(pageRecommend));
    }

    @Override
    public CommonResult<Void> updateRecommendInfo(Long recommendId, Integer recommendStatus) {
        adminAuthUtil.adminAuth();
        if (
                recommendId == null ||
                        recommendStatus == null ||
                        (!CommonConstants.RECOMMEND_STATUS_ENABLE.equals(recommendStatus) &&
                                !CommonConstants.RECOMMEND_STATUS_DISABLE.equals(recommendStatus))
        ) {
            throw new BusinessException(ResultCodeEnum.PARAM_NOT_VALID);
        }

        AdminRecommend selectOne = adminRecommendMapper.selectOne(
                new LambdaQueryWrapper<AdminRecommend>()
                        .select(AdminRecommend::getId)
                        .eq(AdminRecommend::getId, recommendId)
        );

        if (selectOne == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND, "推荐信息不存在");
        }

        AdminRecommend adminRecommend = new AdminRecommend();
        adminRecommend.setId(recommendId);
        adminRecommend.setRecommendStatus(recommendStatus);

        int i = adminRecommendMapper.updateById(adminRecommend);
        if (i != 1) {
            throw new BusinessException(ResultCodeEnum.FAIL, "修改失败");
        }
        return CommonResult.success();
    }
}
