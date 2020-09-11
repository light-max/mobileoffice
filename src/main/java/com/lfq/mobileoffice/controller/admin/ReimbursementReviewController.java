package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.BillItem;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Reimbursement;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.ReimbursementService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 报销审核控制器
 *
 * @author 李凤强
 */
@Controller
public class ReimbursementReviewController {

    @javax.annotation.Resource
    ReimbursementService service;

    /**
     * 按审核状态分页查询报销记录
     *
     * @param currentPage 当前页
     * @param status      状态
     */
    @GetMapping({"/admin/reimbursement/review", "/admin/reimbursement/review/list",
            "/admin/reimbursement/review/list/{currentPage}"})
    @ViewModelParameter(key = "view", value = "reimbursement-review")
    public String list(
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) Integer status,
            Model model
    ) {
        Page<Reimbursement> page = service.listPage(currentPage, status);
        List<Reimbursement> list = page.getRecords();
        Pager pager = new Pager(page, "/admin/reimbursement/review/list");
        if (status != null) {
            pager.setTailAppend("?status=" + status);
        }
        Map<Integer, Employee> employeeMap = service.employeeMap(list);
        model.addAttribute("list", list);
        model.addAttribute("pager", pager);
        model.addAttribute("status", status);
        model.addAttribute("employeeMap", employeeMap);
        return "/admin/reimbursementreview/list";
    }

    /**
     * 查询报销申请的附件
     *
     * @param reimbursementId 报销记录id
     */
    @GetMapping("/reimbursement/file/{reimbursementId}")
    @ResponseBody
    public Response<List<Resource>> resources(@PathVariable Long reimbursementId) {
        List<Resource> resources = service.getResources(reimbursementId);
        GlobalConstant.resourceEmpty.notNull(resources);
        return Response.success(resources);
    }

    /**
     * 查询报销申请中的账单
     *
     * @param reimbursementId 报销记录id
     */
    @GetMapping("/reimbursement/bills/{reimbursementId}")
    @ResponseBody
    public Response<List<BillItem>> bills(@PathVariable Long reimbursementId) {
        return Response.success(service.getBills(reimbursementId));
    }

    @PostMapping("//admin/reimbursement/review/approve/{reimbursementId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void approve(@PathVariable Long reimbursementId) {
        service.approve(reimbursementId);
    }

    @PostMapping("//admin/reimbursement/review/refuse/{reimbursementId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void refuse(@PathVariable Long reimbursementId) {
        service.refuse(reimbursementId);
    }

    @PostMapping("//admin/reimbursement/review/approveall")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void approveAll() {
        service.approveAll();
    }

    @PostMapping("//admin/reimbursement/review/refuseall")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void refuseAll() {
        service.refuseAll();
    }

}
