package com.lfq.mobileoffice.config;

//import com.google.common.base.Predicates;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
////@Configuration
////@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket docker(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                 .apiInfo(webApiInfo())
//                .groupName("lfq")
//                .select()
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))// 错误路径不监控
//                .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
//                .build();
//    }
//
//    private ApiInfo webApiInfo(){
//        return new ApiInfoBuilder()
//                .title("移动办公微应用后台系统-API文档")
//                .description("本文档描述了系统接口")
//                .version("1.0")
//                .contact(new Contact("lfq", "", "3045033935@qq.com"))
//                .build();
//    }
//}