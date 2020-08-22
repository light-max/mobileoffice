package com.lfq.mobileoffice.config;

import com.lfq.mobileoffice.config.interceptor.AdminInterceptor;
import com.lfq.mobileoffice.config.interceptor.GlobalInterceptor;
import com.lfq.mobileoffice.config.interceptor.SystemAdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
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
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("/admin/home");
        registry.addViewController("/sys").setViewName("/sysadmin/home");
        registry.addViewController("/sys/update_admin_success").setViewName("/sysadmin/admin/success");
    }
}
