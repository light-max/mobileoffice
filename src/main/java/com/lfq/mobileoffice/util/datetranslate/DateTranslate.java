package com.lfq.mobileoffice.util.datetranslate;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

/**
 * 日期格式化注解的接口
 *
 * @author 李凤强
 * @see DateParameter
 */
public interface DateTranslate {

    /**
     * 把类中标识了{@link DateParameter}注解的字段转换成对应{@link DateParameter#value()}的格式
     *
     * @return 格式化后的字符串日期
     * @throws RuntimeException 如果没有找到这个类中有{@link DateParameter}注解的字段就会抛出异常
     */
    default String translateDate() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            DateParameter dateParameter = field.getAnnotation(DateParameter.class);
            if (dateParameter != null) {
                try {
                    field.setAccessible(true);
                    Object o = field.get(this);
                    return new SimpleDateFormat(dateParameter.value()).format(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new RuntimeException("没有找到有: " + DateParameter.class + "注解的字段");
    }
}
