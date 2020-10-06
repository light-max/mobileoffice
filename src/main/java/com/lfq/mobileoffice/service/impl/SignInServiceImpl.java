package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.DepartmentMapper;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.mapper.SignInMapper;
import com.lfq.mobileoffice.mapper.SignInTimeMapper;
import com.lfq.mobileoffice.model.data.response.SignInSuccess;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.SignIn;
import com.lfq.mobileoffice.model.entity.SignInTime;
import com.lfq.mobileoffice.service.SignInService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.List;

@Service
public class SignInServiceImpl implements SignInService {

    private final int ONE_DAY = 1000 * 60 * 60 * 24;

    @Resource
    SignInTimeMapper signInTimeMapper;

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    SignInMapper signInMapper;

    @Resource
    EmployeeMapper employeeMapper;

    @Override
    public SignInTime current(Integer departmentId) {
        LambdaQueryWrapper<SignInTime> wrapper = new QueryWrapper<SignInTime>()
                .lambda()
                .eq(SignInTime::getDepartmentId, departmentId)
                .orderByDesc(SignInTime::getCreateTime)
                .last("limit 1");
        return signInTimeMapper.selectOne(wrapper);
    }

    @Override
    public List<SignInTime> departmentAll(Integer departmentId) {
        LambdaQueryWrapper<SignInTime> wrapper = new QueryWrapper<SignInTime>()
                .lambda()
                .eq(SignInTime::getDepartmentId, departmentId)
                .orderByDesc(SignInTime::getCreateTime);
        return signInTimeMapper.selectList(wrapper);
    }

    @Override
    public SignInTime setSignInTime(Integer departmentId, String before, String after) {
        GlobalConstant.departmentNotExists.notNull(
                departmentMapper.selectById(departmentId)
        );
        GlobalConstant.signinTimeNotSelectTime.isFalse(
                before.trim().isEmpty()
        );
        GlobalConstant.signinTimeNotSelectTime.isFalse(
                after.trim().isEmpty()
        );
        GlobalConstant.signinTimeBefore.isTrue(
                LocalTime.parse(before).isBefore(LocalTime.parse(after))
        );
        SignInTime signInTime;
        LambdaQueryWrapper<SignInTime> wrapper = new QueryWrapper<SignInTime>().lambda()
                .eq(SignInTime::getDepartmentId, departmentId)
                .eq(SignInTime::getStart, before)
                .eq(SignInTime::getEnd, after);
        signInTime = signInTimeMapper.selectOne(wrapper);
        if (signInTime == null) {
            signInTime = SignInTime.builder()
                    .departmentId(departmentId)
                    .start(before)
                    .end(after)
                    .build();
            signInTimeMapper.insert(signInTime);
            signInTime.setCreateTime(System.currentTimeMillis());
        } else {
            signInTime.setCreateTime(System.currentTimeMillis());
            signInTimeMapper.updateById(signInTime);
        }
        return signInTime;
    }

    @Override
    public SignInSuccess signIn(Integer employeeId, Integer type) {
        // 检查员工是否存在
        Employee employee = employeeMapper.selectById(employeeId);
        GlobalConstant.employeeNotExist.notNull(employee);
        // 检查员工部门是否存在
        Department department = departmentMapper.selectById(employee.getDepartment());
        GlobalConstant.signinEmployeeNoDepartment.notNull(department);
        // 检查员工所在部门是否有签到规则
        SignInTime current = current(department.getId());
        GlobalConstant.signinDepartmentNotSigninTime.notNull(current);
        // 检查今天是否已经签过到的wrapper
        long currentDayTime = System.currentTimeMillis() / ONE_DAY * ONE_DAY;
        LambdaQueryWrapper<SignIn> wrapper = new QueryWrapper<SignIn>()
                .lambda()
                .eq(SignIn::getEmployeeId, employee.getId())
                .eq(SignIn::getSignInTimeId, current.getId())
                .ge(SignIn::getCreateTime, currentDayTime);
        if (type == 1 || type == 2) {
            SignIn signIn = SignIn.builder()
                    .employeeId(employeeId)
                    .type(type)
                    .signInTimeId(current.getId())
                    .build();
            // 检查上班签到的条件
            if (type == 1) {
                // 检查今天上班是否已签到
                wrapper.eq(SignIn::getType, 1);
                // 签过到就抛出异常
                GlobalConstant.signinTypeError1.isTrue(
                        signInMapper.selectCount(wrapper) < 1
                );
            }
            // 下检查班签到的条件
            else {
                // 检查今天上班是否未签到
                LambdaQueryWrapper<SignIn> eq = wrapper.clone()
                        .eq(SignIn::getType, 1);
                // 没有签上班到就抛出异常
                GlobalConstant.signinTypeError3.isTrue(
                        signInMapper.selectCount(eq) > 0
                );
                // 检查今天下班是否已签过到
                wrapper.eq(SignIn::getType, 2);
                // 下班签过到了就抛出异常
                GlobalConstant.signinTypeError2.isTrue(
                        signInMapper.selectCount(wrapper) < 1
                );
            }
            signInMapper.insert(signIn);
            return new SignInSuccess(signIn, department.getName(), employee.getName());
        } else {
            throw new AssertException(GlobalConstant.signinTypeError.getMessage());
        }
    }
}
