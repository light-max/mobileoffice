package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.entity.Notice;

/**
 * 通知服务
 *
 * @author 李凤强
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 添加一条通知
     *
     * @param notice
     * @throws AssertException
     */
    void addNotice(Notice notice) throws AssertException;

    /**
     * 更新通知的内容
     *
     * @param notice
     * @throws AssertException
     */
    void updateNotice(Notice notice) throws AssertException;
}
