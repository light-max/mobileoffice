package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.AFLTypeMapper;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.AFLType;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.model.entity.WRFL;
import com.lfq.mobileoffice.service.LeaveService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 请假审核控制器
 *
 * @author 李凤强
 * @see com.lfq.mobileoffice.controller.employee.LeaveApplicationController
 */
@Controller
public class LeaveReviewController {

    @javax.annotation.Resource
    LeaveService leaveService;

    @javax.annotation.Resource
    AFLTypeMapper aflTypeMapper;

    /**
     * 分页查询请假列表
     *
     * @param currentPage 当前页
     * @param status      状态
     */
    @GetMapping({"/admin/leave/review", "/admin/leave/review/list",
            "/admin/leave/review/list/{currentPage}"})
    @ViewModelParameter(key = "view", value = "leave-review")
    public String list(
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) Integer status,
            Model model
    ) {
        Page<WRFL> page = leaveService.listPage(currentPage, status);
        List<WRFL> list = page.getRecords();
        Pager pager = new Pager(page, "/admin/leave/review/list");
        if (status != null) pager.setTailAppend("?status=" + status);
        Map<Integer, Employee> employeeMap = leaveService.employeeMap(list);
        Map<Integer, String> typesMap = leaveService.typesMap();
        model.addAttribute("list", list);
        model.addAttribute("pager", pager);
        model.addAttribute("status", status);
        model.addAttribute("employeeMap", employeeMap);
        model.addAttribute("typesMap", typesMap);
        return "/admin/leavereview/list";
    }

    /**
     * 按请假条id查询请假条所带的附件
     *
     * @param wrflId 请假条id
     */
    @GetMapping("/leave/file/{wrflId}")
    @ResponseBody
    public Response<List<Resource>> resources(@PathVariable Long wrflId) {
        List<Resource> resources = leaveService.getResources(wrflId);
        GlobalConstant.resourceEmpty.notNull(resources);
        return Response.success(resources);
    }

    /**
     * 同意一条请假申请
     *
     * @param wrflId 请假条id
     */
    @PostMapping("/admin/leave/review/approve/{wrflId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void approve(@PathVariable Long wrflId) {
        leaveService.approve(wrflId);
    }

    /**
     * 拒绝一条请假申请
     *
     * @param wrflId 请假条id
     */
    @PostMapping("/admin/leave/review/refuse/{wrflId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void refuse(@PathVariable Long wrflId) {
        leaveService.refuse(wrflId);
    }

    /**
     * 同意所有请假申请
     */
    @PostMapping("/admin/leave/review/approveall")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void approveAll() {
        leaveService.approveAll();
    }

    /**
     * 拒绝所有请假申请
     */
    @PostMapping("/admin/leave/review/refuseall")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void refuseAll() {
        leaveService.refuseAll();
    }

    /**
     * 查询请假类型
     */
    @GetMapping("/admin/leave/review/types")
    @ResponseBody
    public Response<List<AFLType>> types() {
        return Response.success(aflTypeMapper.selectList(Wrappers.emptyWrapper()));
    }

    /**
     * 添加请假类型
     */
    @PostMapping("/admin/leave/review/type")
    @ResponseBody
    public Response<AFLType> addType(String name) {
        return Response.success(leaveService.addType(name));
    }

    /**
     * 删除请假类型
     */
    @DeleteMapping("/admin/leave/review/type/{typeId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void deleteType(@PathVariable Integer typeId){
        leaveService.deleteType(typeId);
    }
}
