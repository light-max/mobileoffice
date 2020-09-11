package com.lfq.mobileoffice.controller.employee;

import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.request.ReimbursementPostData;
import com.lfq.mobileoffice.model.entity.BillItem;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Reimbursement;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.ReimbursementService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 员工报销控制器
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/reimbursement/application")
public class ReimbursementController {

    Resource resource;

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
        System.out.println(data);
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
}
