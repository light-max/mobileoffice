package com.lfq.mobileoffice.controller.sysadmin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.entity.Admin;
import com.lfq.mobileoffice.service.AdminService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员账号管理控制器
 *
 * @author 李凤强
 */
@Controller
public class AdminsController {
    @Resource
    AdminService adminService;

    /**
     * 分页查询所有管理员
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     */
    @GetMapping({"/sys/admin/list", "/sys/admin/list/{currentPage}"})
    @ViewModelParameter(key = "view", value = "list")
    public String list(Model model, @PathVariable(value = "currentPage", required = false) Long currentPage) {
        Page<Admin> page = adminService.page(new Page<>(currentPage == null ? 1 : currentPage, 10));
        List<Admin> admins = page.getRecords();
        Pager pager = new Pager(page, "/sys/admin/list");
        model.addAttribute("pager", pager);
        model.addAttribute("admins", admins);
        return "/sysadmin/admin/list";
    }

    /**
     * 添加一个管理员账号
     */
    @PostMapping("/sys/admin")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void add(Admin admin) {
        adminService.addAdmin(admin);
    }

    /**
     * 根据id删除管理员账号
     */
    @DeleteMapping("/sys/admin/{id}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable("id") Integer id) {
        adminService.removeById(id);
    }

    /**
     * 根据id跳转到修改管理员账号的界面
     */
    @GetMapping("/sys/admin/{id}")
    public String toUpdatePage(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("admin", adminService.getById(id));
        return "/sysadmin/admin/update";
    }

    /**
     * 修改管理员账号信息的请求
     */
    @PutMapping("/sys/admin/{id}")
    public String update(Model model, Admin admin) {
        try {
            adminService.updateInfo(admin);
            return "redirect:/sysadmin/admin/success";
        } catch (AssertException e) {
            model.addAttribute("admin", admin);
            model.addAttribute("error_info", e.getMessage());
            return "/sysadmin/admin/update";
        }
    }

    /**
     * 提交修改管理员密码的请求
     *
     * @param pwd 表单中应该包含三个pwd字段<br>
     *            pwd[0]: 旧密码<br>
     *            pwd[1]: 新密码<br>
     *            pwd[2]: 确认密码<br>
     */
    @PostMapping("/sys/admin/pwd/{id}")
    public String pwd(Model model, @PathVariable("id") Integer id, String[] pwd) {
        try {
            adminService.setPwd(id, pwd);
            return "redirect:/sys/update_admin_success";
        } catch (AssertException e) {
            model.addAttribute("admin", adminService.getById(id));
            model.addAttribute("error_pwd", e.getMessage());
            model.addAttribute("pwd", pwd);
            return "/sysadmin/admin/update";
        }
    }
}
