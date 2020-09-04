package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.entity.Admin;

/**
 * 管理员服务
 *
 * @author 李凤强
 */
public interface AdminService extends IService<Admin> {

    /**
     * 添加一个管理员
     *
     * @param admin
     * @throws AssertException
     */
    void addAdmin(Admin admin) throws AssertException;

    /**
     * 更新管理员信息
     *
     * @param admin
     * @throws AssertException
     */
    void updateInfo(Admin admin) throws AssertException;

    /**
     * 为管理员设置新密码
     *
     * @param id  管理员id
     * @param pwd 表单中应该包含三个pwd字段<br>
     *            pwd[0]: 旧密码<br>
     *            pwd[1]: 新密码<br>
     *            pwd[2]: 确认密码<br>
     * @throws AssertException
     */
    void setPwd(Integer id, String[] pwd) throws AssertException;
}
