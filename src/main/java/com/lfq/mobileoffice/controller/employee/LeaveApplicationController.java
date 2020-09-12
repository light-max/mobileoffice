package com.lfq.mobileoffice.controller.employee;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.mapper.AFLTypeMapper;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.PagerData;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.data.response.WRFL_TypeName;
import com.lfq.mobileoffice.model.entity.AFLType;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.model.entity.WRFL;
import com.lfq.mobileoffice.service.LeaveService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @javax.annotation.Resource
    LeaveService leaveService;

    @javax.annotation.Resource
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
                     @RequestParam(value = "resource", required = false) String[] resources,
                     Integer type, String des, Long start, Long end) {
        WRFL wrfl = WRFL.builder()
                .type(type)
                .des(des)
                .start(start)
                .end(end)
                .build();
        leaveService.postLeaveRequest(employee.getId(), wrfl, resources);
    }

    /**
     * 分页查询请假记录
     *
     * @param currentPage 当前页
     * @param status      状态
     * @param employee
     * @return {@link WRFL_TypeName}
     */
    @GetMapping({"list", "list/{currentPage}"})
    @ResponseBody
    public Response<PagerData> list(
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) Integer status,
            @SessionAttribute("employee") Employee employee
    ) {
        Page<WRFL> page = leaveService.listPage(employee.getId(), currentPage, status);
        Pager pager = new Pager(page, "/leave/application");
        return Response.pager(pager, new ArrayList<WRFL_TypeName>() {{
            for (WRFL wrfl : page.getRecords()) {
                String typeName = aflTypeMapper.selectById(wrfl.getType()).getName();
                List<Resource> resources = leaveService.getResources(wrfl.getId());
                add(new WRFL_TypeName(wrfl, typeName, resources));
            }
        }});
    }
}
