package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lfq.mobileoffice.mapper.DepartmentMapper;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.mapper.SignInMapper;
import com.lfq.mobileoffice.mapper.SignInTimeMapper;
import com.lfq.mobileoffice.model.data.response.Attendance;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.SignIn;
import com.lfq.mobileoffice.model.entity.SignInTime;
import com.lfq.mobileoffice.service.AttendanceService;
import com.lfq.mobileoffice.service.SignInService;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final int ONE_DAY = 1000 * 60 * 60 * 24;
    private final int TIME_DIFFERENCE = 1000 * 60 * 60 * 8;

    @Resource
    SignInTimeMapper signInTimeMapper;

    @Resource
    EmployeeMapper employeeMapper;

    @Resource
    SignInMapper signInMapper;

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    SignInService signInService;

    @Override
    public List<Attendance> list(int department, long date) {
        List<Attendance> list = new ArrayList<>();
        // 这一天的开始时间和结束时间
        long start = (date + TIME_DIFFERENCE) / ONE_DAY * ONE_DAY - TIME_DIFFERENCE;
        long end = ((date + TIME_DIFFERENCE) / ONE_DAY + 1) * ONE_DAY - TIME_DIFFERENCE;
        // 查询该部门所有员工，这些员工需要在这个date之前入职
        List<Employee> employees = employeeMapper
                .selectList(new QueryWrapper<Employee>()
                        .lambda()
                        .eq(Employee::getDepartment, department)
                        .le(Employee::getCreateTime, date)
                );
        // 查询这天的签到时间
        SignInTime signInTime_ = signInService.getByDate(department, date);
        for (Employee employee : employees) {
            // 创建签到时间大于这天的开始时间，小于这天的结束时间的查询条件
            LambdaQueryWrapper<SignIn> wrapper = new QueryWrapper<SignIn>()
                    .lambda()
                    .eq(SignIn::getEmployeeId, employee.getId())
                    .gt(SignIn::getCreateTime, start)
                    .lt(SignIn::getCreateTime, end);
            // 查询上班时间
            SignIn toWork = signInMapper.selectOne(wrapper.clone().eq(SignIn::getType, 1));
            // 查询下班时间
            SignIn offWork = signInMapper.selectOne(wrapper.clone().eq(SignIn::getType, 2));
            // 查询签到规则
            SignInTime signInTime;
            if (toWork != null) {
                signInTime = signInTimeMapper.selectById(toWork.getSignInTimeId());
            } else {
                signInTime = signInTime_;
            }
            Attendance attendance = new Attendance(employee, signInTime, toWork, offWork);
            list.add(attendance);
        }
        return list;
    }

    @Override
    public XSSFWorkbook export(int department, long date) {
        List<Attendance> list = list(department, date);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(12);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        XSSFRow headRow = sheet.createRow(0);
        headRow.createCell(0).setCellValue("考勤表");
        XSSFCellStyle headStyle = workbook.createCellStyle();
        XSSFFont headFont = workbook.createFont();
        headFont.setFontHeightInPoints((short) 24);
        headStyle.setFont(headFont);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headRow.getCell(0).setCellStyle(headStyle);

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
        XSSFRow iniRow = sheet.createRow(1);
        iniRow.createCell(0).setCellValue("部门");
        iniRow.createCell(1).setCellValue(departmentMapper.selectById(department).getName());
        iniRow.createCell(3).setCellValue("日期");
        iniRow.createCell(4).setCellValue(new SimpleDateFormat("yyyy年MM月dd日").format(new Date(date)));

        int i = 0;
        XSSFRow tHeadRow = sheet.createRow(2);
        tHeadRow.createCell(i++).setCellValue("员工ID");
        tHeadRow.createCell(i++).setCellValue("员工");
        tHeadRow.createCell(i++).setCellValue("上班");
        tHeadRow.createCell(i++).setCellValue("下班");
        tHeadRow.createCell(i++).setCellValue("考勤时间");
        tHeadRow.createCell(i).setCellValue("状态");

        int r = 3;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        for (Attendance a : list) {
            i = 0;
            XSSFRow row = sheet.createRow(r++);
            row.createCell(i++).setCellValue(String.valueOf(a.getEmployeeId()));
            row.createCell(i++).setCellValue(a.getEmployeeName());
            String toWork = a.getToWork() != null ? format.format(new Date(a.getToWork())) : "";
            row.createCell(i++).setCellValue(toWork);
            String offWork = a.getOffWork() != null ? format.format(new Date(a.getOffWork())) : "";
            row.createCell(i++).setCellValue(offWork);
            row.createCell(i++).setCellValue(a.getAttendanceTime());
            row.createCell(i).setCellValue(a.getStatusString0());
        }
        return workbook;
    }
}
