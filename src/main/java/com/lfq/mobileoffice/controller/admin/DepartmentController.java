package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

/**
 * 部门管理控制器
 *
 * @author 李凤强
 */
@Controller
public class DepartmentController {

    @Resource
    DepartmentService departmentService;

    @Resource
    EmployeeService employeeService;

    /**
     * 分页查询部门
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     */
    @GetMapping({"/admin/department/list", "/admin/department", "/admin/department/list/{currentPage}"})
    @ViewModelParameter(key = "view", value = "department")
    public String list(Model model, @PathVariable(required = false) Integer currentPage) {
        Page<Department> page = departmentService.page(new Page<>(currentPage == null ? 1 : currentPage, 15));
        List<Department> departments = page.getRecords();
        Pager pager = new Pager(page, "/admin/notice/list/");
        model.addAttribute("pager", pager);
        model.addAttribute("departments", departments);
        return "/admin/department/list";
    }

    /**
     * 根据id查询部门
     *
     * @throws com.lfq.mobileoffice.constant.AssertException 如果没有这条记录就抛出异常
     */
    @GetMapping({"/admin/department/{departmentId}", "/department/{departmentId}"})
    @ResponseBody
    public Response<Department> getOne(@PathVariable("departmentId") Integer id) {
        Department department = departmentService.getById(id);
        GlobalConstant.departmentNotExists.notNull(department);
        return Response.success(department);
    }

    /**
     * 添加一个部门
     */
    @PostMapping("/admin/department")
    @ResponseBody
    public Response<Department> add(Department department) {
        departmentService.addDepartment(department);
        return Response.success(department);
    }

    /**
     * 更新部门信息
     */
    @PutMapping("/admin/department")
    @ResponseBody
    public Response<Department> update(Department department) {
        departmentService.updateDepartment(department);
        return Response.success(department);
    }

    /**
     * 根据id删除部门
     */
    @DeleteMapping("/admin/department/{departmentId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer departmentId) {
        GlobalConstant.departmentEmployeeBind.isTrue(
                employeeService.count(new QueryWrapper<Employee>()
                        .eq("department", departmentId)
                ) == 0
        );
        departmentService.removeById(departmentId);
    }

    @GetMapping("test")
    @ResponseBody
    public Response<List<Department>> test() {
        return Response.success(departmentService.list());
    }
}