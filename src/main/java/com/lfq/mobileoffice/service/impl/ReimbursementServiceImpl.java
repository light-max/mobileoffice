package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.*;
import com.lfq.mobileoffice.model.entity.*;
import com.lfq.mobileoffice.service.ReimbursementService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReimbursementServiceImpl extends ServiceImpl<ReimbursementMapper, Reimbursement> implements ReimbursementService {

    @javax.annotation.Resource
    ResourceMapper resourceMapper;

    @javax.annotation.Resource
    BillItemMapper billItemMapper;

    @javax.annotation.Resource
    RFileMapper rFileMapper;

    @javax.annotation.Resource
    EmployeeMapper employeeMapper;

    @Override
    public void postReimbursement(
            Integer employeeId, Reimbursement reimbursement,
            List<BillItem> billItems, String[] resources
    ) {
        // 检查报销基本信息
        GlobalConstant.reimburDesFormat.isTrue(
                reimbursement.getDes().length() <= 200
        );
        GlobalConstant.reimburPayeeNameFormat.isTrue(
                reimbursement.getPayeeName().matches(".{2,100}")
        );
        GlobalConstant.reimburBankCardFormat.isTrue(
                reimbursement.getBankCard().length() <= 30
        );

        // 检查账单项目基本信息
        for (BillItem item : billItems) {
            GlobalConstant.reimburBillItemNameFormat.isTrue(
                    item.getName().matches(".{1,200}")
            );
            GlobalConstant.reimburAmountFormat.isTrue(
                    item.getAmount().compareTo(BigDecimal.valueOf(999999.99)) < 1
            );
        }

        // 检查资源文件是否存在
        if (resources != null) {
            for (String resource : resources) {
                GlobalConstant.resourceNotExists.notNull(
                        resourceMapper.selectById(resource)
                );
            }
        }

        // 把报销记录插入到数据库
        reimbursement.setEmployeeId(employeeId);
        save(reimbursement);

        // 把账单项目插入到数据库
        for (BillItem billItem : billItems) {
            billItem.setReimbursementId(reimbursement.getId());
            billItemMapper.insert(billItem);
        }

        // 把资源文件和报销表关联
        if (resources != null) {
            for (String resource : resources) {
                ReimbursementFile build = ReimbursementFile.builder()
                        .reimbursementId(reimbursement.getId())
                        .resourceId(resource)
                        .build();
                rFileMapper.insert(build);
            }
        }
    }

    @Override
    public Page<Reimbursement> listPage(Integer currentPage, Integer status) {
        LambdaQueryWrapper<Reimbursement> wrapper = new QueryWrapper<Reimbursement>()
                .lambda()
                .orderByDesc(Reimbursement::getCreateTime);
        if (status != null) {
            wrapper.eq(Reimbursement::getStatus, status);
        }
        return page(new Page<>(currentPage == null ? 1 : currentPage, 15), wrapper);
    }

    @Override
    public Page<Reimbursement> listPage(Integer employeeId, Integer currentPage, Integer status) {
        LambdaQueryWrapper<Reimbursement> wrapper = new QueryWrapper<Reimbursement>()
                .lambda()
                .orderByDesc(Reimbursement::getCreateTime)
                .eq(Reimbursement::getEmployeeId, employeeId);
        if (status != null) {
            wrapper.eq(Reimbursement::getStatus, status);
            // 查询所有
            if (status == 1) {
                return page(new Page<>(1, Integer.MAX_VALUE), wrapper);
            }
        }
        return page(new Page<>(currentPage == null ? 1 : currentPage, 15), wrapper);
    }

    @Override
    public Map<Integer, Employee> employeeMap(List<Reimbursement> reimbursements) {
        Set<Integer> ids = reimbursements.stream()
                .map(Reimbursement::getEmployeeId)
                .collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        return new HashMap<>() {{
            for (Employee employee : employeeMapper.selectBatchIds(ids)) {
                put(employee.getId(), employee);
            }
        }};
    }

    @Override
    public List<Resource> getResources(Long reimbursementId) {
        LambdaQueryWrapper<ReimbursementFile> wrapper = new QueryWrapper<ReimbursementFile>()
                .lambda()
                .eq(ReimbursementFile::getReimbursementId, reimbursementId);
        Set<String> ids = rFileMapper.selectList(wrapper)
                .stream()
                .map(ReimbursementFile::getResourceId)
                .collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return null;
        }
        return resourceMapper.selectBatchIds(ids);
    }

    @Override
    public List<BillItem> getBills(Long reimbursementId) {
        LambdaQueryWrapper<BillItem> wrapper = new QueryWrapper<BillItem>()
                .lambda()
                .eq(BillItem::getReimbursementId, reimbursementId);
        return billItemMapper.selectList(wrapper);
    }

    @Override
    public void approve(Long reimbursementId) {
        LambdaUpdateWrapper<Reimbursement> wrapper = new UpdateWrapper<Reimbursement>()
                .lambda()
                .eq(Reimbursement::getId, reimbursementId)
                .eq(Reimbursement::getStatus, 1)
                .set(Reimbursement::getStatus, 2);
        update(wrapper);
    }

    @Override
    public void refuse(Long reimbursementId) {
        LambdaUpdateWrapper<Reimbursement> wrapper = new UpdateWrapper<Reimbursement>()
                .lambda()
                .eq(Reimbursement::getId, reimbursementId)
                .eq(Reimbursement::getStatus, 1)
                .set(Reimbursement::getStatus, 3);
        update(wrapper);
    }

    @Override
    public void approveAll() {
        LambdaUpdateWrapper<Reimbursement> wrapper = new UpdateWrapper<Reimbursement>()
                .lambda()
                .eq(Reimbursement::getStatus, 1)
                .set(Reimbursement::getStatus, 2);
        update(wrapper);
    }

    @Override
    public void refuseAll() {
        LambdaUpdateWrapper<Reimbursement> wrapper = new UpdateWrapper<Reimbursement>()
                .lambda()
                .eq(Reimbursement::getStatus, 1)
                .set(Reimbursement::getStatus, 3);
        update(wrapper);
    }
}
