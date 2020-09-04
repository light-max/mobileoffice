package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.service.DepartmentService;
import com.lfq.mobileoffice.service.EmployeeService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 员工管理控制器
 *
 * @author 李凤强
 */
@Controller
public class EmployeeController {

    @Resource
    EmployeeService employeeService;

    @Resource
    DepartmentService departmentService;

    /**
     * 分页查询员工
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     * @param department  查询指定部门的员工，为空时查询所有员工
     */
    @GetMapping({"/admin/employee/list", "/admin/employee", "/admin/employee/list/{currentPage}"})
    @ViewModelParameter(key = "view", value = "employee")
    public String list(Model model,
                       @PathVariable(required = false) Integer currentPage,
                       @RequestParam(required = false) Integer department
    ) {
        Page<Employee> page = employeeService.listPage(currentPage, department);
        List<Employee> employees = page.getRecords();
        Map<Integer, Department> departmentMap = employeeService.departmentMap(employees);
        Pager pager = new Pager(page, "/admin/employee/list");
        if (department != null) {
            pager.setTailAppend("?department=" + department);
        }
        List<Department> departments = departmentService.list();
        model.addAttribute("pager", pager);
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        model.addAttribute("department", department);
        model.addAttribute("departmentMap", departmentMap);
        return "/admin/employee/list";
    }

    /**
     * 添加员工
     *
     * @param employee
     */
    @PostMapping("/admin/employee")
    @ResponseBody
    public Response<Employee> add(Employee employee) {
        employeeService.addEmployee(employee);
        return Response.success(employee);
    }

    /**
     * 获取一个员工的信息
     *
     * @param employeeId
     */
    @GetMapping({"/admin/employee/{employeeId}", "/employee/{employeeId}"})
    @ResponseBody
    public Response<Employee> getOne(@PathVariable Integer employeeId) {
        Employee employee = employeeService.getById(employeeId);
        GlobalConstant.employeeNotExist.notNull(employee);
        return Response.success(employee);
    }

    /**
     * 更新员工信息
     */
    @PutMapping("/admin/employee")
    @ResponseBody
    public Response<Employee> update(Employee employee) {
        employeeService.updateEmployee(employee);
        return Response.success(employee);
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/admin/employee/{employeeId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    /**
     * 重置员工密码
     */
    @PutMapping("/admin/employee/resetpwd/{employeeId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void resetPwd(@PathVariable Integer employeeId) {
        employeeService.updatePwd(employeeId, "1");
    }
}
