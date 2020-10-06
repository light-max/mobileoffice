package com.lfq.mobileoffice.controller.employee;

import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 员工修改信息控制器
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/employee/info")
public class EmployeeInfoController {

    @Resource
    EmployeeService service;

    /**
     * 修改居住地
     */
    @PutMapping("address")
    @ResponseBody
    public Response<String> address(
            @SessionAttribute("employee") Employee employee,
            String value
    ) {
        return Response.success(service.updateAddress(employee.getId(), value));
    }

    /**
     * 修改联系方式
     */
    @PutMapping("contact")
    @ResponseBody
    public Response<String> contact(
            @SessionAttribute("employee")Employee employee,
            String value
    ){
        return Response.success(service.updateContact(employee.getId(), value));
    }
}
