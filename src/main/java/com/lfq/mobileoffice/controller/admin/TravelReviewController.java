package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.BusinessTrip;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.TravelService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 出差审批控制器
 *
 * @author 李凤强
 */
@Controller
public class TravelReviewController {

    @javax.annotation.Resource
    TravelService travelService;

    /**
     * 分页查询出差列表
     *
     * @param currentPage 当前页
     * @param status      状态
     */
    @GetMapping({"/admin/travel/review", "/admin/travel/review/list",
            "/admin/travel/review/list/{currentPage}"})
    @ViewModelParameter(key = "view", value = "travel-review")
    public String list(
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) Integer status,
            Model model
    ) {
        Page<BusinessTrip> page = travelService.listPage(currentPage, status);
        List<BusinessTrip> list = page.getRecords();
        Pager pager = new Pager(page, "/admin/travel/review/list");
        if (status != null) {
            pager.setTailAppend("?status=" + status);
        }
        Map<Integer, Employee> employeeMap = travelService.employeeMap(list);
        model.addAttribute("list", list);
        model.addAttribute("pager", pager);
        model.addAttribute("status", status);
        model.addAttribute("employeeMap", employeeMap);
        return "/admin/travelreview/list";
    }

    /**
     * 按出差表id查询所带的附件
     *
     * @param travelId 出差id
     */
    @GetMapping("/travel/file/{travelId}")
    @ResponseBody
    public Response<List<Resource>> resources(@PathVariable Long travelId) {
        List<Resource> resources = travelService.getResources(travelId);
        GlobalConstant.resourceEmpty.notNull(resources);
        return Response.success(resources);
    }

    /**
     * 同意一条出差申请
     *
     * @param travelId 出差id
     */
    @PostMapping("/admin/travel/review/approve/{travelId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void approve(@PathVariable Long travelId) {
        travelService.approve(travelId);
    }

    /**
     * 拒绝一条出差申请
     *
     * @param travelId 出差id
     */
    @PostMapping("/admin/travel/review/refuse/{travelId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void refuse(@PathVariable Long travelId) {
        travelService.refuse(travelId);
    }

    /**
     * 同意所有出差申请
     */
    @PostMapping("/admin/travel/review/approveall")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void approveAll() {
        travelService.approveAll();
    }

    /**
     * 拒绝所有出差申请
     */
    @PostMapping("/admin/travel/review/refuseall")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void refuseAll() {
        travelService.refuseAll();
    }
}
