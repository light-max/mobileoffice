package com.lfq.mobileoffice.controller.admin;

import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Admin;
import com.lfq.mobileoffice.service.LoginService;
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
 * 管理员登陆请求
 */
@Controller
public class ALoginController {
    @Resource
    LoginService loginService;

    /**
     * 访问登陆页面
     */
    @GetMapping("/admin/login")
    @ViewModelParameter(key = "role", value = "管理员")
    public String login(Model model) {
        return "/login";
    }

    /**
     * 提交登陆请求
     *
     * @param session  用于保存{@link Admin}已登录的管理员实例
     * @param username 用户名
     * @param pwd      密码
     */
    @PostMapping("/admin/login")
    public String login(Model model, HttpSession session, String username, String pwd) {
        Response<Admin> response = loginService.admin(username, pwd);
        if (response.isSuccess()) {
            session.setAttribute("admin", response.getData());
            return "redirect:/admin";
        } else {
            login(model);
            model.addAttribute("username", username);
            model.addAttribute("pwd", pwd);
            model.addAttribute("error", response.getMessage());
            return "/login";
        }
    }

    /**
     * 退出登陆，并且重定向到登陆页面
     */
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/admin/login";
    }

    /**
     * 用户未登录的提示页面
     */
    @GetMapping(value = "/admin/notloggedin", produces = "text/html")
    @ViewModelParameters({
            @ViewModelParameter(key = "action", value = "/admin/login"),
            @ViewModelParameter(key = "role", value = "管理员")
    })
    public String notLogin(Model model) {
        model.addAttribute("error", GlobalConstant.noAccess.getMessage());
        return "/login";
    }

    /**
     * 返回用户未登录的错误信息
     */
    @GetMapping("/admin/notloggedin")
    @ResponseBody
    public Response<Object> notLogin() {
        return Response.error(GlobalConstant.noAccess.getMessage());
    }
}
