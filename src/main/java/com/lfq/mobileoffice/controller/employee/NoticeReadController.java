package com.lfq.mobileoffice.controller.employee;

import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 通知是否已读控制器
 *
 * @author 李凤强
 */
@Controller
public class NoticeReadController {

    @Resource
    NoticeService noticeService;

    /**
     * 查询是否已读某条公告
     *
     * @return true:是 false:否
     */
    @GetMapping("/employee/notice/isread/{noticeId}")
    @ResponseBody
    public Response<Boolean> isRead(
            @SessionAttribute("employee") Employee employee,
            @PathVariable Integer noticeId
    ) {
        return Response.success(noticeService
                .isEmployeeReadNotice(employee.getId(), noticeId)
        );
    }

    @PostMapping("/employee/notice/read/{noticeId}")
    @ResponseBody
    public Response<Boolean> read(
            @SessionAttribute("employee") Employee employee,
            @PathVariable Integer noticeId
    ) {
        return Response.success(noticeService
                .employeeReadNotice(employee.getId(), noticeId)
        );
    }
}
