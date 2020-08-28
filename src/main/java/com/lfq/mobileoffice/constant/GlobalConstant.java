package com.lfq.mobileoffice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 一些用于响应请求的全局常量
 */
@Getter
@AllArgsConstructor
public enum GlobalConstant implements Assert {
    error("系统错误"),

    noAccess("没有访问权限，请重新登录"),

    usernameExists("用户名已存在"),
    usernameFormat("用户名格式错误，只能使用英文字母和数字何下划线，并且长度在4-16位"),
    passwordFormat("密码格式错误，只能使用英文字母、数字、下划线和小数点，并且长度在4-16位"),
    desFormat("账号描述不能不能超过120个字"),

    loginError("登陆错误，用户名或密码不正确"),
    accountDisable("登陆失败，此账号已被禁用"),

    sourcePasswordError("原密码错误"),
    passwordsError("两次输入的密码不一致"),

    noticeTitleFormat("公告标题不能超过100个字"),
    noticeContentFormat("公告内容字数在1~1000以内"),

    noticeNotExist("公告不存在"),

    departmentNotExists("这个部门不存在"),
    departmentNameFormat("部门名称必须在1~100个字以内"),
    departmentDesFormat("部门描述不能超过300个字"),
    departmentEmployeeBind("部门下还有员工，不能删除"),

    employeeNotExist("员工不存在"),
    employeeNameFormat("员工姓名必须在2~100个字以内"),
    employeeAddressFormat("员工现居住地不能超过300个字符"),
    employeeContactFormat("员工联系方式不能超过20个字符"),
    employeePwdFormat("密码必须在1~16位，只能使用字母和数字"),

    roomNameFormat("会议室名称必须在1~20个字符内"),
    roomLocationFormat("会议室地址描述不能超过200个字符"),
    roomCapacityFormat("人数必须在1~500之间"),
    roomNotExists("会议室不存在"),

    equipmentNameFormat("设备名称必须在1~30个字符内"),
    equipmentDesFormat("设备描述不能超过100个字符"),
    equipmentNotExists("设备不存在"),

    roomApplyTime("会议室使用结束时间必须在开始之后"),
    roomApplyTime1("不能选择已经过去的时间"),
    roomApplyDesFormat("备注不能超过200个字符"),
    roomApplyDuplicateTimePeriod("时间段不能与其他申请重复"),
    ;

    private String message;
}
