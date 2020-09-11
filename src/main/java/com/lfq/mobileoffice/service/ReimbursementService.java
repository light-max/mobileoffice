package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.model.entity.BillItem;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Reimbursement;
import com.lfq.mobileoffice.model.entity.Resource;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 账单报销服务
 *
 * @author 李凤强
 */
public interface ReimbursementService extends IService<Reimbursement> {
    /**
     * 提交一条报销记录
     *
     * @param employeeId    员工id
     * @param reimbursement 报销申请信息
     * @param billItems     报销申请中的账单项目
     * @param resources     报销记录中的附件资源id
     */
    void postReimbursement(
            Integer employeeId, Reimbursement reimbursement,
            List<BillItem> billItems, @Nullable String[] resources
    );

    /**
     * 按审核状态分页查询报销记录
     *
     * @param currentPage 当前页
     * @param status      状态
     * @return
     */
    Page<Reimbursement> listPage(@Nullable Integer currentPage, @Nullable Integer status);

    /**
     * 查询报销记录申请中所有的员工信息
     *
     * @param reimbursements 包含了员工信息的报销记录
     * @return key:{@link Reimbursement#getId()}, value:{@link Reimbursement}
     */
    Map<Integer, Employee> employeeMap(List<Reimbursement> reimbursements);

    /**
     * 按报销id查询附件
     *
     * @param reimbursementId 报销id
     * @return 如果没有附件就返回null
     */
    @Nullable
    List<Resource> getResources(Long reimbursementId);

    /**
     * 按报销id查询账单
     *
     * @param reimbursementId 报销id
     */
    List<BillItem> getBills(Long reimbursementId);

    /**
     * 同意一条报销申请
     *
     * @param reimbursementId 报销id
     */
    void approve(Long reimbursementId);

    /**
     * 拒绝一条报销申请
     *
     * @param reimbursementId 报销id
     */
    void refuse(Long reimbursementId);

    /**
     * 同意所有报销申请
     */
    void approveAll();

    /**
     * 拒绝所有报销申请
     */
    void refuseAll();
}
