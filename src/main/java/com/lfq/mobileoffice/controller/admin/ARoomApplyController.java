package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Room;
import com.lfq.mobileoffice.model.entity.RoomApply;
import com.lfq.mobileoffice.service.RoomApplyService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 管理员会议室申请控制器
 */
@Controller
public class ARoomApplyController {

    @Resource
    RoomApplyService roomApplyService;

    /**
     * 分页查询会议室预约申请
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     */
    @GetMapping(value = {"/admin/apply/room/list", "/admin/apply/room", "/admin/apply/room/list/{currentPage}"},
            produces = "text/html")
    @ViewModelParameter(key = "view", value = "room-apply")
    public String list(
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) Integer status,
            Model model
    ) {
        Page<RoomApply> page = roomApplyService.listPage(currentPage, status);
        List<RoomApply> list = page.getRecords();
        Pager pager = new Pager(page, "/admin/apply/room/list");
        if (status != null) pager.setTailAppend("?status=" + status);
        Map<Integer, Room> roomMap = roomApplyService.roomMap(list);
        Map<Integer, Employee> employeeMap = roomApplyService.employeeMap(list);
        model.addAttribute("list", list);
        model.addAttribute("pager", pager);
        model.addAttribute("status", status);
        model.addAttribute("roomMap", roomMap);
        model.addAttribute("employeeMap", employeeMap);
        return "/admin/roomapply/list";
    }

    /**
     * 同意某一条预约申请
     *
     * @param applyId 预约申请的id
     */
    @PostMapping("/admin/apply/room/approve/{applyId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void approve(@PathVariable Integer applyId) {
        roomApplyService.updateStatus(applyId, 2);
    }

    /**
     * 拒绝某一条预约申请
     *
     * @param applyId 预约申请的id
     */
    @PostMapping("/admin/apply/room/refuse/{applyId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void refuse(@PathVariable Integer applyId) {
        roomApplyService.updateStatus(applyId, 3);
    }
}
