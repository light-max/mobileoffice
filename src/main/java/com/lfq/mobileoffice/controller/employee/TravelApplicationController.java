package com.lfq.mobileoffice.controller.employee;

import com.lfq.mobileoffice.model.entity.BusinessTrip;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.service.TravelService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 员工出差申请控制器
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/travel/application")
public class TravelApplicationController {

    @Resource
    TravelService travelService;

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
        travelService.postTravel(employee.getId(), trip, resources);
    }
}
