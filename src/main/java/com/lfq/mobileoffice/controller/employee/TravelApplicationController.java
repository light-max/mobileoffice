package com.lfq.mobileoffice.controller.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.PagerData;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.data.response.BusinessTrip_Resources;
import com.lfq.mobileoffice.model.entity.BusinessTrip;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.TravelService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 员工出差申请控制器
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/travel/application")
public class TravelApplicationController {

    @javax.annotation.Resource
    TravelService service;

    /**
     * 提交一个出差申请
     */
    @PostMapping("post")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void post(@SessionAttribute("employee") Employee employee,
                     @RequestParam(value = "resource", required = false) String[] resources,
                     String des, String address, Long start, Long end) {
        System.out.println(Arrays.toString(resources));
        BusinessTrip trip = BusinessTrip.builder()
                .des(des)
                .address(address)
                .start(start)
                .end(end)
                .build();
        service.postTravel(employee.getId(), trip, resources);
    }

    /**
     * 按状态分页查询员工出差申请记录
     * @param currentPage 当前页
     * @param status 状态
     * @param employee
     * @return
     */
    @GetMapping({"list","list/{currentPage}"})
    @ResponseBody
    public Response<PagerData> list(
            @PathVariable(required = false)Integer currentPage,
            @RequestParam(required = false)Integer status,
            @SessionAttribute("employee") Employee employee
    ){
        Page<BusinessTrip> page = service.listPage(employee.getId(), currentPage, status);
        Pager pager = new Pager(page, "/travel/application/list");
        return Response.pager(pager,new ArrayList<BusinessTrip_Resources>(){{
            for (BusinessTrip trip : page.getRecords()) {
                List<Resource> resources = service.getResources(trip.getId());
                add(new BusinessTrip_Resources(trip,resources));
            }
        }});
    }
}
