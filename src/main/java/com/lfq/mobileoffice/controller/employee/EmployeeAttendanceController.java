package com.lfq.mobileoffice.controller.employee;

import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.data.response.EmployeeAttendance;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.service.AttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工考勤控制器
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/employee/attendance")
public class EmployeeAttendanceController {

    @Resource
    AttendanceService attendanceService;

    /**
     * 员工按年与月获取某个月的考勤
     */
    @GetMapping("/month/{year}/{month}")
    @ResponseBody
    public Response<List<EmployeeAttendance>> find(
            @SessionAttribute("employee") Employee employee,
            @PathVariable Integer year,
            @PathVariable Integer month
    ) {
        return Response.success(attendanceService.employee(employee.getId(), year, month));
    }
}
