package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.*;
import com.lfq.mobileoffice.model.entity.AFLType;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.WRFL;
import com.lfq.mobileoffice.model.entity.WRFLFile;
import com.lfq.mobileoffice.service.LeaveService;
import org.springframework.stereotype.Service;

import com.lfq.mobileoffice.model.entity.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl extends ServiceImpl<WRFLMapper, WRFL> implements LeaveService {

    @javax.annotation.Resource
    AFLTypeMapper aflTypeMapper;

    @javax.annotation.Resource
    ResourceMapper resourceMapper;

    @javax.annotation.Resource
    WRFLFileMapper wrflFileMapper;

    @javax.annotation.Resource
    EmployeeMapper employeeMapper;

    @Override
    public void postLeaveRequest(Integer employeeId, WRFL wrfl, String[] resources) {
        // 检查请假的基本信息
        GlobalConstant.wrflTime.isTrue(
                wrfl.getStart() < wrfl.getEnd()
        );
        GlobalConstant.wrflTypeNotExists.notNull(
                aflTypeMapper.selectById(wrfl.getType())
        );
        GlobalConstant.wrflDesFormat.isTrue(
                wrfl.getDes().length() < 200
        );

        // 检查资源是否存在
        if (resources != null) {
            for (String resource : resources) {
                GlobalConstant.resourceNotExists.notNull(
                        resourceMapper.selectById(resource)
                );
            }
        }

        // 把请假申请插入到数据库
        WRFL build = WRFL.builder().type(wrfl.getType())
                .employeeId(employeeId)
                .start(wrfl.getStart())
                .end(wrfl.getEnd())
                .des(wrfl.getDes())
                .build();
        save(build);

        // 把资源id与请假条id关联
        if (resources != null) {
            for (String resource : resources) {
                WRFLFile wrflFile = WRFLFile.builder()
                        .wrflId(build.getId())
                        .resourceId(resource)
                        .build();
                wrflFileMapper.insert(wrflFile);
            }
        }
    }

    @Override
    public Page<WRFL> listPage(Integer currentPage, Integer status) {
        LambdaQueryWrapper<WRFL> wrapper = new QueryWrapper<WRFL>()
                .lambda()
                .orderByDesc(WRFL::getCreateTime);
        if (status != null) {
            wrapper.eq(WRFL::getStatus, status);
        }
        return page(new Page<>(currentPage == null ? 1 : currentPage, 15), wrapper);
    }

    @Override
    public Map<Integer, Employee> employeeMap(List<WRFL> wrfls) {
        Set<Integer> ids = wrfls.stream()
                .map(WRFL::getEmployeeId)
                .collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        List<Employee> employees = employeeMapper.selectBatchIds(ids);
        return new HashMap<>() {{
            for (Employee employee : employees) {
                put(employee.getId(), employee);
            }
        }};
    }

    @Override
    public Map<Integer, String> typesMap() {
        return new HashMap<>() {{
            for (AFLType type : aflTypeMapper.selectList(Wrappers.emptyWrapper())) {
                put(type.getId(), type.getName());
            }
        }};
    }

    @Override
    public List<Resource> getResources(Long wrflId) {
        LambdaQueryWrapper<WRFLFile> wrapper = new QueryWrapper<WRFLFile>()
                .lambda()
                .eq(WRFLFile::getWrflId, wrflId);
        List<String> resourceIds = wrflFileMapper.selectList(wrapper)
                .stream()
                .map(WRFLFile::getResourceId)
                .collect(Collectors.toList());
        if (resourceIds.isEmpty()) {
            return null;
        }
        return resourceMapper.selectBatchIds(resourceIds);
    }

    @Override
    public void approve(Long wrflId) {
        LambdaUpdateWrapper<WRFL> wrapper = new UpdateWrapper<WRFL>()
                .lambda()
                .eq(WRFL::getId, wrflId)
                .eq(WRFL::getId, 1)
                .set(WRFL::getStatus, 2);
        update(wrapper);
    }

    @Override
    public void refuse(Long wrflId) {
        LambdaUpdateWrapper<WRFL> wrapper = new UpdateWrapper<WRFL>()
                .lambda()
                .eq(WRFL::getId, wrflId)
                .eq(WRFL::getId, 1)
                .set(WRFL::getStatus, 3);
        update(wrapper);
    }

    @Override
    public void approveAll() {
        LambdaUpdateWrapper<WRFL> wrapper = new UpdateWrapper<WRFL>()
                .lambda()
                .eq(WRFL::getStatus, 1)
                .set(WRFL::getStatus, 2);
        update(wrapper);
    }

    @Override
    public void refuseAll() {
        LambdaUpdateWrapper<WRFL> wrapper = new UpdateWrapper<WRFL>()
                .lambda()
                .eq(WRFL::getStatus, 1)
                .set(WRFL::getStatus, 3);
        update(wrapper);
    }

    @Override
    public AFLType addType(String name) {
        GlobalConstant.wrflTypeFormat.isTrue(
                name.matches(".{2,10}")
        );
        GlobalConstant.wrflTypeExists.isNull(aflTypeMapper.selectOne(
                new QueryWrapper<AFLType>().lambda().eq(AFLType::getName, name)
        ));
        AFLType aflType = new AFLType(null, name);
        aflTypeMapper.insert(aflType);
        return aflType;
    }

    @Override
    public void deleteType(Integer typeId) {
        LambdaQueryWrapper<WRFL> queryWrapper = new QueryWrapper<WRFL>()
                .lambda()
                .eq(WRFL::getType, typeId);
        GlobalConstant.wrflTypeBind.isNull(getOne(queryWrapper));
        aflTypeMapper.deleteById(typeId);
    }
}
