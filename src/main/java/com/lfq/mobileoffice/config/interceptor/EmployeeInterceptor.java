package com.lfq.mobileoffice.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 员工未登录拦截器
 *
 * @author 李凤强
 */
public class EmployeeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object admin = request.getSession().getAttribute("employee");
        if (admin == null) {
            response.sendRedirect("/employee/notloggedin");
            return false;
        }
        return true;
    }
}
