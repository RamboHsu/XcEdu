package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 自定义异常类
 */
public class CustomException extends RuntimeException {
    //异常代码信息
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        //异常信息为：错误代码+错误信息
        super("错误代码："+resultCode.code()+" 错误信息："+resultCode.message());
        this.resultCode=resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
