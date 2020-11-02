package com.lfq.mobileoffice.controller.admin;

import com.lfq.mobileoffice.model.data.response.Attendance;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.service.AttendanceService;
import com.lfq.mobileoffice.service.DepartmentService;
import com.lfq.mobileoffice.util.WorkbookTools;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 考勤管理控制器
 *
 * @author 李凤强
 */
@Controller
public class AttendanceController {

    @Resource
    DepartmentService departmentService;

    @Resource
    AttendanceService attendanceService;

    /**
     * 根据部门与日期查询部门的考勤
     */
    @GetMapping({"/admin/attendance/list/{currentPage}", "/admin/attendance/list",
            "/admin/attendance"})
    @ViewModelParameter(key = "view", value = "attendance")
    public String list(
            @RequestParam(value = "department", required = false) Integer department,
            @RequestParam(value = "date", required = false) Date date,
            Model model
    ) {
        List<Department> departments = departmentService.list();
        if (!departments.isEmpty()) {
            if (department == null) {
                department = departments.get(0).getId();
            }
            date = date == null ? new Date() : date;
            List<Attendance> attendances = attendanceService.list(department, date.getTime());
            model.addAttribute("attendances", attendances);
            model.addAttribute("departments", departments);
            model.addAttribute("department", department);
            model.addAttribute("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        return "/admin/attendance/list";
    }

    /**
     * 根据部门id下载某一天的考勤表
     */
    @GetMapping("/attendance/excel/{department}")
    @ResponseBody
    public ResponseEntity<byte[]> export(@PathVariable Integer department, Date date) throws IOException {
        XSSFWorkbook workbook = attendanceService.export(department, date.getTime());
        Department d = departmentService.getById(department);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return WorkbookTools.toResponseEntity(workbook,
                "(考勤表)" + d.getName() + format.format(date)
        );
    }
}
