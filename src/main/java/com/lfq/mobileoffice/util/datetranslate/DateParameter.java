package com.lfq.mobileoffice.util.datetranslate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义日期格式化注解
 *
 * @see DateTranslate
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateParameter {
    String value() default "YYYY年MM月dd日 HH:mm:ss";
}
