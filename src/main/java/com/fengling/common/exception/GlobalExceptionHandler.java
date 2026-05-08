package com.fengling.common.exception;

import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.resp.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public CommonResult<Void> handleBusinessException(BusinessException ex){
        return CommonResult.fail(ex.getCode(), ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception ex){
        return CommonResult.fail(ResultCodeEnum.FAIL,"系统异常");
    }
}
