package com.lfq.mobileoffice.util;

import com.lfq.mobileoffice.model.data.Response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识了这个注解的请求的void方法意味着需要返回一个{@link Response#success()}结果
 *
 * @author 李凤强
 * @see com.lfq.mobileoffice.config.interceptor.GlobalInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseDefaultSuccessResponse {
}
