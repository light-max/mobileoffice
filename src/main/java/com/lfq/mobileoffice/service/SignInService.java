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
     * 按部门id查询某一天的签到时间，如果这一天的签到时间没有，就返回当前的签到时间
     *
     * @param department 部门id
     * @param date       日期
     * @return
     */
    SignInTime getByDate(int department, long date);

    /**
     * 根据部门id获取此部门所有签到时间
     *
     * @param departmentId 部门id
     * @return
     */
    List<SignInTime> departmentAll(Integer departmentId);

    /**
     * 设置新的签到时间<br>
     * <b>如果今天已经有人签到了，就只能等明天再更新了</b><br>
     * <b>如果是以前设置过的时间则会覆盖</b>
     * <b>如果当前的签到时间没人签到，就会被删除</b>
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
