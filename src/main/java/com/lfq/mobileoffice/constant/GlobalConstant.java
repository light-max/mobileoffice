package com.lfq.mobileoffice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 一些用于响应请求的全局常量
 *
 * @author 李凤强
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
    employeeNameFormat("员工姓名必须在1~100个字以内"),
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

    wrflTime("开始时间必须在结束时间之前"),
    wrflTypeNotExists("请假类型不存在，请重新选择"),
    wrflDesFormat("请假原因不能多余200个字符"),
    wrflTypeExists("已有这个类型的假期"),
    wrflTypeFormat("类型名称必须在2~10个字符以内"),
    wrflTypeBind("该类型下有请假申请，不能删除"),

    resourceNotExists("资源文件不存在"),
    resourceEmpty("没有资源文件"),

    btTime("开始时间必须在结束时间之前"),
    btDesFormat("出差事由不能超过200个字符"),
    btAddress("出差地址不能超过200个字符"),

    reimburDesFormat("报销详情不能超过200个字符"),
    reimburPayeeNameFormat("收款人姓名必须在2~100个字符内"),
    reimburBankCardFormat("收款银行卡号不能超过30个字符"),
    reimburAmountFormat("报销金额不能超过999999.99元"),
    reimburBillItemNameFormat("单条账单项目名必须在1~200个字符以内"),

    signinTimeNotExists("这个部门还没有设置签到时间"),
    signinTimeNotSelectTime("请选择签到时间"),
    signinTimeBefore("上班签到必须在下班签到之前"),

    signinEmployeeNoDepartment("员工还没有分配部门，无法签到"),
    signinDepartmentNotSigninTime("员工所在部门还未设置签到规则，无法签到"),
    signinTypeError("签到类型错误，请上报维护人员"),
    signinTypeError1("今天上班已经签过到，不可重复签到"),
    signinTypeError2("今天下班已经签过到，不可重复签到"),
    signinTypeError3("下班签到之前还未进行上班签到"),

    templateFileFormat("模板文件格式错误"),
    templateIniRowNotExists("工作簿中没有找到配置行"),
    templateIniDepartmentName("部门id与部门名称不匹配"),
    templateIniDepartmentIdFormat("配置中的id格式错误"),
    templateIniDepartmentNameFormat("配置中的部门名称格式错误"),
    templateDepartmentNotExists("目标部门不存在"),
    templateEmpty("这个模板是空的，请重新上传"),
    ;

    private String message;
}
