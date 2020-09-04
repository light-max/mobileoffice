package com.lfq.mobileoffice.exception;

import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 *
 * @author 李凤强
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理断言异常
     */
    @ExceptionHandler(AssertException.class)
    @ResponseBody
    public Response<Object> handlerAssertException(AssertException e) {
        return Response.error(e.getMessage());
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<Object> handlerException(Exception e) {
        e.printStackTrace();
        return Response.error(GlobalConstant.error.getMessage() + ": " + e.getMessage());
    }
}