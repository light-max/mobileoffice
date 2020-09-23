package com.lfq.mobileoffice.controller.employee;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.PagerData;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.RoomApply;
import com.lfq.mobileoffice.service.RoomApplyService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 员工会议室申请控制器
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/apply/room")
public class ERoomApplyController {

    @Resource
    RoomApplyService roomApplyService;

    /**
     * 员工分页查询自己的申请记录
     *
     * @see RoomApplyService#employeeListPage(Integer, Integer, Integer)
     */
    @GetMapping({"/list", "/list/{currentPage}"})
    @ResponseBody
    public Response<PagerData> list(
            @SessionAttribute Employee employee,
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) Integer status
    ) {
        Page<RoomApply> page = roomApplyService.employeeListPage(employee.getId(), currentPage, status);
        Pager pager = new Pager(page, "/apply/room/list");
        return Response.pager(pager, page.getRecords());
    }

    /**
     * 员工分页查询某个会议室所有的申请记录
     */
    @GetMapping({"/toroom/list", "/toroom/list/{currentPage}"})
    @ResponseBody
    public Response<PagerData> list(@PathVariable(required = false) Integer currentPage, Integer roomId) {
        LambdaQueryWrapper<RoomApply> wrapper = new QueryWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getRoomId, roomId)
                // 按开始时间进行排序
                .orderByDesc(RoomApply::getStart);
        Page<RoomApply> page = roomApplyService.page(new Page<>(currentPage == null ? 1 : currentPage, 15), wrapper);
        Pager pager = new Pager(page, "/apply/room/toroom/list");
        return Response.pager(pager, page.getRecords());
    }

    /**
     * 提交一个申请
     */
    @PostMapping("post")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void post(@SessionAttribute Employee employee, RoomApply roomApply) {
        roomApplyService.postApply(employee.getId(), roomApply);
    }


}
