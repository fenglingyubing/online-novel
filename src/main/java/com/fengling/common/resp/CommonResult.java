package com.fengling.common.resp;

import com.fengling.common.constant.ResultCodeEnum;
import lombok.Getter;

@Getter
public class CommonResult<T> {

    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    private CommonResult(){
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.message = ResultCodeEnum.SUCCESS.getMessage();
    }

    private CommonResult(T data){
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.message = ResultCodeEnum.SUCCESS.getMessage();
        this.data = data;
    }

    private CommonResult(ResultCodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    private CommonResult(ResultCodeEnum codeEnum, String message){
        this.code = codeEnum.getCode();
        this.message = message;
    }

    private CommonResult(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    /**
     * 业务成功，无数据返回
     * @return
     */
    public static CommonResult<Void> success() {
        return new CommonResult<>();
    }

    /**
     * 业务成功，有数据返回
     * @param data
     * @return
     * @param <T>
     */
    public static <T> CommonResult<T> success(T data){
        return new CommonResult<>(data);
    }

    /**
     * 业务失败
     * @param resultCodeEnum
     * @return
     */
    public static CommonResult<Void> fail(ResultCodeEnum resultCodeEnum){
        return new CommonResult<>(resultCodeEnum);
    }

    /**
     * 业务失败，可自定义返回消息
     * @param resultCodeEnum
     * @param message
     * @return
     */
    public static CommonResult<Void> fail(ResultCodeEnum resultCodeEnum, String message){
        return new CommonResult<>(resultCodeEnum, message);
    }

    /**
     * 业务失败，可自定义响应码和响应消息
     * @param code
     * @param message
     * @return
     */
    public static CommonResult<Void> fail(Integer code, String message){
        return new CommonResult<>(code, message);
    }
}
