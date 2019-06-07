package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


//使用Spring中提供的 @ControllerAdvice和@ExceptionHandler注解便可实现全局异常处理。
@ControllerAdvice
public class ExceptionCatch {
    //定义异常记录日志
    private static final Logger LOGGER=LoggerFactory.getLogger(ExceptionCatch.class);

    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Exception>,ResultCode> EXCEPTION;

    //使用builder来构建一个异常类型和错误代码的异常Map
    private static ImmutableMap.Builder<Class<? extends Exception>, ResultCode> builder= ImmutableMap.builder();

    static {
        //在类初始化时，map构造器中加入一些基础的异常类型判断
        builder.put( HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM );
    }


    //捕获Exception异常
    /**
     * 捕获Controller的不可预知Exception异常
     * @param e Exception Exception异常信息
     * @return ResponseResult 接口规范定义的响应结果类
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e) {
        //1.记录日志
        LOGGER.error("catch exception:{}",e.getMessage());
        //将异常信息转成相应的异常信息，并使用ResponseResult返回
        //2.判断异常Map是否为空
        if (ObjectUtils.isEmpty(EXCEPTION)){
            //异常Map为空，使用构造器创建一个ImmutableMap
            EXCEPTION = builder.build();
        }
        //3.根据异常类型获取ResultCode
        ResultCode resultCode = EXCEPTION.get(e.getClass());
        //声明接口规范定义定义的响应结果类
        ResponseResult result = null;
        //4.判断resultCode
        if (ObjectUtils.isEmpty(resultCode)){
            //如果为空，将给出服务器错误，代码99999
            result = new ResponseResult(CommonCode.SERVER_ERROR);
        }else {
            //如果不为空，将resultCode通过响应结果对象放回
            result=new ResponseResult(resultCode);
        }
        //5.返回结果对象
        return result;
    }


    //捕获CustomException异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        ResultCode resultCode = e.getResultCode();
        ResponseResult responseResult = new ResponseResult(resultCode);

        return responseResult;
    }

}
