package com.lfq.mobileoffice.controller.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.PagerData;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.data.request.ReimbursementPostData;
import com.lfq.mobileoffice.model.data.response.ReimbursementBean;
import com.lfq.mobileoffice.model.entity.BillItem;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Reimbursement;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.ReimbursementService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 员工报销控制器
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/reimbursement/application")
public class ReimbursementController {

    @javax.annotation.Resource
    ReimbursementService service;

    /**
     * 员工提交报销申请
     */
    @PostMapping("post")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void post(
            @SessionAttribute("employee") Employee employee,
            @RequestBody ReimbursementPostData data
    ) {
        Reimbursement reimbursement = Reimbursement.builder()
                .des(data.getDes())
                .payeeName(data.getPayee())
                .bankCard(data.getCard())
                .build();
        service.postReimbursement(employee.getId(), reimbursement, data.getBillItems(), data.getResources());
    }

    /**
     * 检查{@link BillItem#getName()}与{@link BillItem#getAmount()}是否符合规则
     *
     * @param billItem 检查对象
     */
    @PostMapping("/billitem/check")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void checkBillItem(@RequestBody BillItem billItem) {
        System.out.println(billItem);
        GlobalConstant.reimburBillItemNameFormat.isTrue(
                billItem.getName().length() <= 200
        );
        GlobalConstant.reimburAmountFormat.isTrue(
                billItem.getAmount().compareTo(BigDecimal.valueOf(999999.99)) < 1
        );
    }

    /**
     * 按状态分页查询员工报销
     *
     * @param currentPage 当前页
     * @param status      状态
     */
    @GetMapping({"list", "list/{currentPage}"})
    @ResponseBody
    public Response<PagerData> listPage(
            @SessionAttribute("employee") Employee employee,
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) Integer status
    ) {
        Page<Reimbursement> page = service.listPage(employee.getId(), currentPage, status);
        Pager pager = new Pager(page, "/reimbursement/application/list");
        return Response.pager(pager, new ArrayList<ReimbursementBean>() {{
            for (Reimbursement reimbursement : page.getRecords()) {
                List<Resource> resources = service.getResources(reimbursement.getId());
                List<BillItem> billItems = service.getBills(reimbursement.getId());
                add(new ReimbursementBean(reimbursement, resources, billItems));
            }
        }});
    }
}
