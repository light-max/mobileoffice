package com.lfq.mobileoffice.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 检查系统管理员账号是否登陆的拦截器
 */
public class SystemAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object sysadmin = session.getAttribute("sysadmin");
        if (sysadmin == null) {
            response.sendRedirect("/sys/notloggedin");
            return false;
        }
        return true;
    }
}
