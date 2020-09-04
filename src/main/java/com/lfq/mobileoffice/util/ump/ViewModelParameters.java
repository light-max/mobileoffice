package com.lfq.mobileoffice.util.ump;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link ViewModelParameter}的数组形式
 *
 * @author 李凤强
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelParameters {
    ViewModelParameter[] value();
}
