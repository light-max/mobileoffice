package com.lfq.mobileoffice.model.data.response;

import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.Getter;

/**
 * 通知已读的员工信息展示类
 * <b>此数据仅用于前端使用</b>
 *
 * @author 李凤强
 */
@Getter
public class NoticeRead_EmployeeName implements DateTranslate {
    /*** 员工ID */
    private int employeeId;

    /*** 员工姓名 */
    private String employeeName;

    /*** 已读时间 */
    @DateParameter
    private long date;

    public String getDate() {
        return translateDate();
    }

    public NoticeRead_EmployeeName(int employeeId, String employeeName, long date) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.date = date;
    }
}
