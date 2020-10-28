package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Admin;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.TmpEmployee;
import com.lfq.mobileoffice.service.DepartmentService;
import com.lfq.mobileoffice.service.EmployeeService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.WorkbookTools;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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

    /**
     * 员工模板excel表, 不带部门
     */
    @GetMapping("/employee/batch/template")
    @ResponseBody
    public ResponseEntity<byte[]> template() throws Exception {
        return WorkbookTools.toResponseEntity(employeeService.template(false), "template");
    }

    /**
     * 员工模板excel表, 附带所有部门
     */
    @GetMapping("/employee/batch/template/depatement")
    @ResponseBody
    public ResponseEntity<byte[]> templateDepartment() throws IOException {
        return WorkbookTools.toResponseEntity(employeeService.template(true), "template");
    }

    /**
     * 提交批量导入员工的模板
     *
     * @param file 模板文件
     */
    @PostMapping("/admin/employee/batch/template")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void template(@SessionAttribute("admin") Admin admin, MultipartFile file) {
        employeeService.importEmployee(admin.getId(), file);
    }

    /**
     * 获取所有导入后待确认添加的员工
     */
    @GetMapping("/admin/employee/batch/tmp")
    @ResponseBody
    public Response<List<TmpEmployee>> employeeTmp(@SessionAttribute("admin") Admin admin) {
        return Response.success(employeeService.employeeTmp(admin.getId()));
    }

    /**
     * 确认导入所有导入后待确认添加的员工
     */
    @PostMapping("/admin/employee/batch/tmp")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void postEmployeeTmp(@SessionAttribute("admin") Admin admin) {
        employeeService.postEmployeeTmp(admin.getId());
    }
}
