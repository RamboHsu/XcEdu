package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 抛出异常工具类
 */
public class ExceptionCast {

    /**
     * 使用此静态方法抛出自定义异常，以减少代码中频繁的要throw一个异常的现象
     * @param resultCode ResultCode 异常代码和异常信息
     */
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
