package com.lfq.mobileoffice.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * MP 自动填充类
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", System.currentTimeMillis(), metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}