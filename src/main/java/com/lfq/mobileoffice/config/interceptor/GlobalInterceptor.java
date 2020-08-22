package com.lfq.mobileoffice.config.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 全局拦截器，处理带有{@link UseDefaultSuccessResponse}注解的请求的返回值，
 * 返回数据{@link Response#success()}
 */
public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            if (!response.isCommitted()) {
                Method method = ((HandlerMethod) handler).getMethod();
                if (method.isAnnotationPresent(UseDefaultSuccessResponse.class) && method.getReturnType().equals(void.class)) {
                    response.setHeader("Content-Type", "application/json;charset=UTF-8");
                    response.getWriter().print(JSONObject.toJSON(Response.success()));
                }
            }
        }
    }
}
