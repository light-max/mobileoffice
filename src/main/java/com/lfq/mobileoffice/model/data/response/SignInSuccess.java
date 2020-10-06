package com.lfq.mobileoffice.model.data.response;

import com.lfq.mobileoffice.model.entity.SignIn;
import lombok.Getter;

/**
 * 签到成功返回数据
 *
 * @author 李凤强
 */
@Getter
public class SignInSuccess extends SignIn {
    /*** 部门名称 */
    private final String departmentName;

    /*** 员工姓名 */
    private final String employeeName;

    /**
     * 签到时间日期
     */
    public String getSignInDateTime() {
        return super.translateDate();
    }

    /**
     * 签到类型字符串
     */
    public String getSignInType() {
        if (getType() == 1) {
            return "上班";
        } else if (getType() == 2) {
            return "下班";
        }
        return "";
    }

    public SignInSuccess(SignIn signIn, String departmentName, String employeeName) {
        setId(signIn.getId());
        setEmployeeId(signIn.getEmployeeId());
        setSignInTimeId(signIn.getSignInTimeId());
        setType(signIn.getType());
        setCreateTime(signIn.getCreateTime());
        this.departmentName = departmentName;
        this.employeeName = employeeName;
    }
}
