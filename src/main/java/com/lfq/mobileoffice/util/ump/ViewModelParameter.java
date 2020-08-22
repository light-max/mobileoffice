package com.lfq.mobileoffice.util.ump;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为controller方法中的{@link org.springframework.ui.Model}添加属性的注解
 *
 * @see com.lfq.mobileoffice.config.aop.ViewModelParameterAop
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelParameter {

    String key();

    String value();
}
