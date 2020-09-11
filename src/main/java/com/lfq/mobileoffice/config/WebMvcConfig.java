package com.lfq.mobileoffice.config;

import com.lfq.mobileoffice.config.interceptor.AdminInterceptor;
import com.lfq.mobileoffice.config.interceptor.EmployeeInterceptor;
import com.lfq.mobileoffice.config.interceptor.GlobalInterceptor;
import com.lfq.mobileoffice.config.interceptor.SystemAdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 *
 * @author 李凤强
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SystemAdminInterceptor())
                .addPathPatterns("/sys/**")
                .excludePathPatterns("/sys/login")
                .excludePathPatterns("/sys/notloggedin");
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/notloggedin");
        registry.addInterceptor(new GlobalInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new EmployeeInterceptor())
                .addPathPatterns("/apply/room/**")// 员工申请会议室
                .addPathPatterns("/leave/application/**")// 员工请假请求
                .addPathPatterns("/employee/resource")// 员工上传&删除文件请求
                .addPathPatterns("/travel/application/**")// 员工出差申请
                .addPathPatterns("/reimbursement/application/**")// 员工报销申请
        ;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("/admin/home");
        registry.addViewController("/sys").setViewName("/sysadmin/home");
        registry.addViewController("/sys/update_admin_success").setViewName("/sysadmin/admin/success");
    }
}
