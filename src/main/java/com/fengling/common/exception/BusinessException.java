package com.fengling.common.exception;

import com.fengling.common.constant.ResultCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    /**
     * 响应码
     */
    private final Integer code;
    /**
     * 响应消息
     */
    private final String message;

    public BusinessException(ResultCodeEnum codeEnum){
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    public BusinessException(ResultCodeEnum codeEnum, String message){
        super(message);
        this.code = codeEnum.getCode();
        this.message = message;
    }
}
