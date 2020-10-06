package com.lfq.mobileoffice.service;

import com.lfq.mobileoffice.model.data.response.SignInSuccess;
import com.lfq.mobileoffice.model.entity.SignInTime;

import java.util.List;

/**
 * 签到服务
 *
 * @author 李凤强
 */
public interface SignInService {
    /**
     * 按照部门id获取当前签到时间
     *
     * @param departmentId 部门id
     * @return 最新签到时间，签到就按照这个时间来
     */
    SignInTime current(Integer departmentId);

    /**
     * 根据部门id获取此部门所有签到时间
     *
     * @param departmentId 部门id
     * @return
     */
    List<SignInTime> departmentAll(Integer departmentId);

    /**
     * 设置新的签到时间<br>
     * <b>如果是以前设置过的时间则会覆盖</b>
     *
     * @param departmentId 会议室id
     * @param before       开始时间
     * @param after        结束时间
     * @return 新的签到时间
     */
    SignInTime setSignInTime(Integer departmentId, String before, String after);

    /**
     * 为员工签到
     *
     * @param employeeId 员工id
     * @return 签到成功的数据
     */
    SignInSuccess signIn(Integer employeeId, Integer type);
}
