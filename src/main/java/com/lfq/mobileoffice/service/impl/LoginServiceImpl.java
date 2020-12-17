package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.AdminMapper;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.mapper.SysAdminMapper;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Admin;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.SysAdmin;
import com.lfq.mobileoffice.service.LoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    SysAdminMapper sysAdminMapper;

    @Resource
    AdminMapper adminMapper;

    @Resource
    EmployeeMapper employeeMapper;

    @Override
    public Response<SysAdmin> sys(String username, String password) {
        LambdaQueryWrapper<SysAdmin> wrapper = new QueryWrapper<SysAdmin>()
                .lambda()
                .eq(SysAdmin::getUsername, username)
                .eq(SysAdmin::getPwd, password);
        SysAdmin sysAdmin = sysAdminMapper.selectOne(wrapper);
        try {
            GlobalConstant.loginError.notNull(sysAdmin);
            return Response.success(sysAdmin);
        } catch (AssertException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response<Admin> admin(String username, String password) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<Admin>()
                .eq("username", username)
                .eq("pwd", password);
        Admin admin = adminMapper.selectOne(wrapper);
        try {
            GlobalConstant.loginError.notNull(admin);
            GlobalConstant.accountDisable.isTrue(admin.getEnable());
            return Response.success(admin);
        } catch (AssertException e) {
            return Response.error(e.getMessage());
        }
    }

    @Override
    public Response<Employee> employee(Integer id, String password) {
        QueryWrapper<Employee> wrapper = new QueryWrapper<Employee>()
                .eq("id", id)
                .eq("pwd", password);
        Employee employee = employeeMapper.selectOne(wrapper);
        try {
            GlobalConstant.loginError.notNull(employee);
            return Response.success(employee);
        } catch (AssertException e) {
            return Response.error(e.getMessage());
        }
    }
}
