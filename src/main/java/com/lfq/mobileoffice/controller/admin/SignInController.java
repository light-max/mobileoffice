package com.lfq.mobileoffice.controller.admin;

import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.data.response.SignInSuccess;
import com.lfq.mobileoffice.model.entity.SignInTime;
import com.lfq.mobileoffice.service.SignInService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门签到控制器
 *
 * @author 李凤强
 */
@Controller
public class SignInController {

    @Resource
    SignInService service;

    /**
     * 按部门id获取当前签到时间
     */
    @GetMapping("/signintime/current")
    @ResponseBody
    public Response<SignInTime> current(Integer department) {
        SignInTime current = service.current(department);
        GlobalConstant.signinTimeNotExists.notNull(current);
        return Response.success(current);
    }

    /**
     * 更新签到时间
     */
    @PutMapping("/admin/signintime")
    @ResponseBody
    public Response<SignInTime> updateSignInTime(Integer department, String before, String after) {
        return Response.success(service.setSignInTime(department, before, after));
    }

    /**
     * 根据部门id查询部门所有设置过的签到时间
     */
    @GetMapping("/signintime/all")
    @ResponseBody
    public Response<List<SignInTime>> all(Integer department) {
        return Response.success(service.departmentAll(department));
    }

    /**
     * 为员工签到
     */
    @PostMapping("/admin/employee/signin")
    @ResponseBody
    @UseDefaultSuccessResponse
    public Response<SignInSuccess> signIn(Integer employee, Integer type) {
        return Response.success(service.signIn(employee, type));
    }
}
