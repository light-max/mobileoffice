package com.lfq.mobileoffice.controller.sysadmin;

import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.SysAdmin;
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
 * 系统管理员登陆控制器
 *
 * @author 李凤强
 */
@Controller
public class SALoginController {
    @Resource
    LoginService loginService;

    /**
     * 访问登陆页面
     */
    @GetMapping("/sys/login")
    @ViewModelParameters({
            @ViewModelParameter(key = "action", value = "/sys/login"),
            @ViewModelParameter(key = "role", value = "系统管理员")
    })
    public String login(Model model) {
        return "/login";
    }

    /**
     * 提交登陆请求
     *
     * @param session  登陆成功后会把{@link SysAdmin}系统管理员实例存入session中
     * @param username 用户名
     * @param pwd      密码
     */
    @PostMapping("/sys/login")
    public String login(Model model, HttpSession session, String username, String pwd) {
        Response<SysAdmin> response = loginService.sys(username, pwd);
        if (response.isSuccess()) {
            session.setAttribute("sysadmin", response.getData());
            return "redirect:/sys";
        } else {
            login(model);
            model.addAttribute("error", response.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("pwd", pwd);
            return "/login";
        }
    }

    /**
     * 退出登陆，并且重定向到登陆页面
     */
    @GetMapping("/sys/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("sysadmin");
        return "redirect:/sys/login";
    }

    /**
     * 用户未登录的提示页面
     */
    @GetMapping(value = "/sys/notloggedin", produces = "text/html")
    @ViewModelParameters({
            @ViewModelParameter(key = "action", value = "/sys/login"),
            @ViewModelParameter(key = "role", value = "系统管理员")
    })
    public String notLogin(Model model) {
        model.addAttribute("error", GlobalConstant.noAccess.getMessage());
        return "/login";
    }

    /**
     * 返回用户未登录的错误信息
     */
    @GetMapping("/sys/notloggedin")
    @ResponseBody
    public Response<Object> notLogin() {
        return Response.error(GlobalConstant.noAccess.getMessage());
    }
}
