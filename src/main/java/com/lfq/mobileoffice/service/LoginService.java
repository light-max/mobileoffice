package com.lfq.mobileoffice.service;

import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Admin;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.SysAdmin;

/**
 * 登陆服务
 *
 * @author 李凤强
 */
public interface LoginService {

    /**
     * 系统管理员登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return 登陆成功会在 {@link Response#getData()}中返回登陆后的管理员实体类
     */
    Response<SysAdmin> sys(String username, String password);

    /**
     * 管理员登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return 登陆成功会在 {@link Response#getData()}中返回登陆后的管理员实体类
     */
    Response<Admin> admin(String username, String password);

    /**
     * 员工
     *
     * @param id       员工id
     * @param password 员工密码
     * @return 登陆成功回在 {@link Response#getData()}中返回登陆后的员工实体类
     */
    Response<Employee> employee(Integer id, String password);
}
