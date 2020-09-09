package com.lfq.mobileoffice.controller.employee;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lfq.mobileoffice.mapper.AFLTypeMapper;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.AFLType;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.WRFL;
import com.lfq.mobileoffice.service.LeaveService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工请假申请控制器
 *
 * @author 李凤强
 * @see com.lfq.mobileoffice.controller.admin.LeaveReviewController
 */
@Controller
@RequestMapping("/leave/application")
public class LeaveApplicationController {

    @Resource
    LeaveService leaveService;

    @Resource
    AFLTypeMapper aflTypeMapper;

    /**
     * 查询请假类型
     */
    @GetMapping("types")
    @ResponseBody
    public Response<List<AFLType>> types() {
        return Response.success(aflTypeMapper.selectList(Wrappers.emptyWrapper()));
    }

    /**
     * 提交一个请假申请
     */
    @PostMapping("post")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void post(@SessionAttribute("employee") Employee employee,
                     @RequestParam("resource") String[] resources,
                     Integer type, String des, Long start, Long end) {
        WRFL wrfl = WRFL.builder()
                .type(type)
                .des(des)
                .start(start)
                .end(end)
                .build();
        leaveService.postLeaveRequest(employee.getId(), wrfl, resources);
    }
}
