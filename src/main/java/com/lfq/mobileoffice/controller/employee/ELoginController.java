package com.lfq.mobileoffice.controller.employee;

import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.service.LoginService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import com.lfq.mobileoffice.util.ump.ViewModelParameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 员工登陆控制器
 *
 * @author 李凤强
 */
@Controller
public class ELoginController {

    @Resource
    LoginService loginService;

    /**
     * 提交登陆请求
     */
    @PostMapping("/employee/login")
    @ResponseBody
    public Response<Employee> login(HttpSession session, Integer id, String pwd) throws InterruptedException {
        Response<Employee> response = loginService.employee(id, pwd);
        if (response.isSuccess()) {
            session.setAttribute("employee", response.getData());
        }
        return response;
    }

    /**
     * 退出登陆
     */
    @PostMapping("/employee/logout")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void logout(HttpSession session) {
        session.removeAttribute("employee");
    }

    /**
     * 提交登陆请求
     */
    @PostMapping(value = "/employee/login", produces = "text/html")
    public String login(HttpSession session, String username, String pwd) {
        Response<Employee> response = loginService.employee(Integer.parseInt(username), pwd);
        if (response.isSuccess()) {
            session.setAttribute("employee", response.getData());
        }
        return "/login";
    }

    /**
     * 用户未登录的提示页面
     */
    @GetMapping(value = "/employee/notloggedin", produces = "text/html")
    @ViewModelParameters({
            @ViewModelParameter(key = "action", value = "/employee/login"),
            @ViewModelParameter(key = "role", value = "员工")
    })
    public String notLogin(Model model) {
        model.addAttribute("error", GlobalConstant.noAccess.getMessage());
        return "/login";
    }

    /**
     * 返回用户未登录的错误信息
     */
    @GetMapping("/employee/notloggedin")
    @ResponseBody
    public Response<Object> notLogin() {
        return Response.denied(GlobalConstant.noAccess.getMessage());
    }
}
