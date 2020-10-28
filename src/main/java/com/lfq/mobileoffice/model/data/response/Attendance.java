package com.lfq.mobileoffice.model.data.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.SignIn;
import com.lfq.mobileoffice.model.entity.SignInTime;
import lombok.Getter;
import lombok.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 考勤数据,包含了两个{@link SignIn}
 *
 * @author 李凤强
 */
@Getter
public class Attendance {
    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    /*** 员工id */
    private Integer employeeId;

    /*** 员工姓名 */
    private String employeeName;

    /*** 上班时间 */
    private Long toWork;

    /*** 下班时间 */
    private Long offWork;

    /*** 应该在这个时间之前签上班到 */
    private String before;

    /*** 应该在这个时间之前签下班到 */
    private String after;

    /**
     * 构造函数
     *
     * @param employee 员工信息
     * @param time     签到时间
     * @param to       上班时间
     * @param off      下班时间
     */
    public Attendance(@NonNull Employee employee, SignInTime time, SignIn to, SignIn off) {
        employeeId = employee.getId();
        employeeName = employee.getName();
        if (time != null) {
            before = time.getStart();
            after = time.getEnd();
        }
        if (to != null) {
            toWork = to.getCreateTime();
        }
        if (off != null) {
            offWork = off.getCreateTime();
        }

        if (to == null && off == null) {
            status = 2;
        } else if (to == null) {
            status = 3;
        } else if (off == null) {
            status = 4;
        } else {
            if (getToWorkString().contains("danger")) {
                status = 5;
            } else if (getOffWorkString().contains("danger")) {
                status = 6;
            } else {
                status = 1;
            }
        }
    }

    /**
     * 状态
     * <ul>
     *     <li>1.正常</li>
     *     <li>2.缺勤</li>
     *     <li>3.上班未签到</li>
     *     <li>4.下班未签到</li>
     *     <li>5.迟到</li>
     *     <li>6.早退</li>
     * </ul>
     */
    private Integer status;

    /**
     * 获取上班时间字符串
     */
    @JsonIgnore
    public String getToWorkString() {
        if (toWork == null || before == null) {
            return "";
        }
        String time = Attendance.format.format(new Date(toWork));
        if (time.compareTo(before) >= 0) {
            return "<span class=\"text-danger\">" + time + "</span>";
        } else {
            return format.format(new Date(toWork));
        }
    }

    /**
     * 获取下班时间字符串
     */
    @JsonIgnore
    public String getOffWorkString() {
        if (offWork == null || before == null) {
            return "";
        }
        String time = Attendance.format.format(new Date(offWork));
        if (time.compareTo(after) <= 0) {
            return "<span class=\"text-danger\">" + time + "</span>";
        } else {
            return format.format(new Date(toWork));
        }
    }

    /**
     * 获取状态字符串
     */
    @JsonIgnore
    public String getStatusString() {
        switch (status) {
            case 1:
                return "<span class=\"text-success\">正常</span>";
            case 2:
                return "<span class=\"text-danger\">未签到</span>";
            case 3:
                return "<span class=\"text-warning\">上班未签到</span>";
            case 4:
                return "<span class=\"text-warning\">下班未签到</span>";
            case 5:
                return "<span class=\"text-warning\">迟到</span>";
            case 6:
                return "<span class=\"text-warning\">早退</span>";
        }
        return "";
    }

    @JsonIgnore
    public String getStatusString0(){
        switch (status) {
            case 1:
                return "正常";
            case 2:
                return "未签到";
            case 3:
                return "上班未签到";
            case 4:
                return "下班未签到";
            case 5:
                return "迟到";
            case 6:
                return "早退";
        }
        return "";
    }

    /**
     * 获取考勤时间字符串
     */
    @JsonIgnore
    public String getAttendanceTime() {
        if (before != null && after != null) {
            return before + "-" + after;
        } else {
            return "";
        }
    }
}
