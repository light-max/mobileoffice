package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.DepartmentMapper;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Override
    public void addDepartment(Department department) throws AssertException {
        GlobalConstant.departmentNameFormat.isTrue(
                department.getName().matches(".{1,100}")
        );
        GlobalConstant.departmentDesFormat.isTrue(
                department.getDes().length() <= 1000
        );
        baseMapper.insert(Department.builder()
                .name(department.getName())
                .des(department.getDes())
                .build());
    }

    @Override
    public void updateDepartment(Department department) throws AssertException {
        GlobalConstant.departmentNameFormat.isTrue(
                department.getName().matches(".{1,100}")
        );
        GlobalConstant.departmentDesFormat.isTrue(
                department.getDes().length() <= 1000
        );
        baseMapper.updateById(Department.builder()
                .id(department.getId())
                .name(department.getName())
                .des(department.getDes())
                .build());
    }
}
