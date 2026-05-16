package com.fengling.common.util;

import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.dto.PageReqDto;
import com.fengling.common.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * 分页校验工具类
 */
@Component
public class PageAuthUtil {

    public void pageAuth(PageReqDto pageReqDto) {
        if (pageReqDto == null) {
            throw new BusinessException(ResultCodeEnum.FAIL);
        }
        Long pageNum = pageReqDto.getPageNum();
        Long pageSize = pageReqDto.getPageSize();
        if (pageNum == null || pageNum <= 0 || pageSize == null || pageSize <= 0) {
            throw new BusinessException(ResultCodeEnum.FAIL);
        }
    }
}
