package com.lfq.mobileoffice.service;

import com.lfq.mobileoffice.model.data.response.Attendance;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/**
 * 考勤服务
 *
 * @author 李凤强
 */
public interface AttendanceService {
    /**
     * 查看部门的考勤
     *
     * @param department 部门id
     * @param date       日期
     * @return
     */
    List<Attendance> list(int department, long date);

    /**
     * 导出部门的考勤为excel表
     *
     * @param department 部门id
     * @param date       日期
     * @return
     */
    XSSFWorkbook export(int department, long date);
}
