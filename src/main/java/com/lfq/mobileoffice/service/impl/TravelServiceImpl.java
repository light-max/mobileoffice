package com.lfq.mobileoffice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.BTFileMapper;
import com.lfq.mobileoffice.mapper.BusinessTripMapper;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.mapper.ResourceMapper;
import com.lfq.mobileoffice.model.entity.BTFile;
import com.lfq.mobileoffice.model.entity.BusinessTrip;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.TravelService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TravelServiceImpl extends ServiceImpl<BusinessTripMapper, BusinessTrip> implements TravelService {

    @javax.annotation.Resource
    ResourceMapper resourceMapper;

    @javax.annotation.Resource
    BTFileMapper btFileMapper;

    @javax.annotation.Resource
    EmployeeMapper employeeMapper;

    @Override
    public void postTravel(Integer employeeId, BusinessTrip trip, String[] resources) {
        // 检查出差信息是否符合要求
        GlobalConstant.btTime.isTrue(
                trip.getStart() < trip.getEnd()
        );
        GlobalConstant.btDesFormat.isTrue(
                trip.getDes().length() <= 200
        );
        GlobalConstant.btAddress.isTrue(
                trip.getAddress().length() <= 200
        );

        // 检查资源文件是否存在
        if (resources != null) {
            for (String resource : resources) {
                GlobalConstant.resourceNotExists.notNull(
                        resourceMapper.selectById(resource)
                );
            }
        }

        // 把出差申请数据插入到数据库
        trip.setEmployeeId(employeeId);
        save(trip);

        // 把出差申id和资源id关联起来
        // 把资源id与请假条id关联
        if (resources != null) {
            for (String resource : resources) {
                BTFile btFile = BTFile.builder()
                        .btId(trip.getId())
                        .resourceId(resource)
                        .build();
                btFileMapper.insert(btFile);
            }
        }
    }

    @Override
    public Page<BusinessTrip> listPage(Integer currentPage, Integer status) {
        LambdaQueryWrapper<BusinessTrip> wrapper = new QueryWrapper<BusinessTrip>()
                .lambda()
                .orderByDesc(BusinessTrip::getCreateTime);
        if (status != null) {
            wrapper.eq(BusinessTrip::getStatus, status);
        }
        return page(new Page<>(currentPage == null ? 1 : currentPage, 10), wrapper);
    }

    @Override
    public Page<BusinessTrip> listPage(Integer employeeId, Integer currentPage, Integer status) {
        LambdaQueryWrapper<BusinessTrip> wrapper = new QueryWrapper<BusinessTrip>()
                .lambda()
                .eq(BusinessTrip::getEmployeeId, employeeId)
                .orderByDesc(BusinessTrip::getCreateTime);
        if (status != null) {
            wrapper.eq(BusinessTrip::getStatus, status);
            // 查询所有
            if (status == 1) {
                return page(new Page<>(1, Integer.MAX_VALUE), wrapper);
            }
        }
        return page(new Page<>(currentPage == null ? 1 : currentPage, 15), wrapper);
    }

    @Override
    public Map<Integer, Employee> employeeMap(List<BusinessTrip> trips) {
        Set<Integer> ids = trips.stream()
                .map(BusinessTrip::getEmployeeId)
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
    public List<Resource> getResources(Long travelId) {
        LambdaQueryWrapper<BTFile> queryWrapper = new QueryWrapper<BTFile>()
                .lambda()
                .eq(BTFile::getBtId, travelId);
        List<String> resourceIds = btFileMapper.selectList(queryWrapper)
                .stream()
                .map(BTFile::getResourceId)
                .collect(Collectors.toList());
        if (resourceIds.isEmpty()) {
            return null;
        }
        return resourceMapper.selectBatchIds(resourceIds);
    }

    @Override
    public void approve(Long btId) {
        LambdaUpdateWrapper<BusinessTrip> wrapper = new UpdateWrapper<BusinessTrip>()
                .lambda()
                .eq(BusinessTrip::getId, btId)
                .eq(BusinessTrip::getStatus, 1)
                .set(BusinessTrip::getStatus, 2);
        update(wrapper);
    }

    @Override
    public void refuse(Long btId) {
        LambdaUpdateWrapper<BusinessTrip> wrapper = new UpdateWrapper<BusinessTrip>()
                .lambda()
                .eq(BusinessTrip::getId, btId)
                .eq(BusinessTrip::getStatus, 1)
                .set(BusinessTrip::getStatus, 3);
        update(wrapper);
    }

    @Override
    public void approveAll() {
        LambdaUpdateWrapper<BusinessTrip> wrapper = new UpdateWrapper<BusinessTrip>()
                .lambda()
                .eq(BusinessTrip::getStatus, 1)
                .set(BusinessTrip::getStatus, 2);
        update(wrapper);
    }

    @Override
    public void refuseAll() {
        LambdaUpdateWrapper<BusinessTrip> wrapper = new UpdateWrapper<BusinessTrip>()
                .lambda()
                .eq(BusinessTrip::getStatus, 1)
                .set(BusinessTrip::getStatus, 3);
        update(wrapper);
    }
}
