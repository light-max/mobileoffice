package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.DepartmentMapper;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.service.EmployeeService;
import com.lfq.mobileoffice.util.IdCardCheckUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 员工管理服务
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    DepartmentMapper departmentMapper;

    @Override
    public Page<Employee> listPage(Integer currentPage, Integer department) {
        Page<Employee> page;
        if (department == null) {
            page = page(new Page<>(currentPage == null ? 1 : currentPage, 10));
        } else {
            page = page(new Page<>(currentPage == null ? 1 : currentPage, 10),
                    new QueryWrapper<Employee>().eq("department", department)
            );
        }
        return page;
    }

    @Override
    public void addEmployee(Employee employee) throws AssertException {
        GlobalConstant.employeeNameFormat.isTrue(
                employee.getName().matches(".{1,100}")
        );
        GlobalConstant.employeeAddressFormat.isTrue(
                employee.getAddress().length() < 300
        );
        GlobalConstant.employeeContactFormat.isTrue(
                employee.getContact().length() < 20
        );
        String validate = IdCardCheckUtil.IDCardValidate(employee.getIdNumber());
        if (validate != null) {
            throw new AssertException(validate);
        }
        String sex = IdCardCheckUtil.isMale(employee.getIdNumber()) ? "male" : "female";
        Employee build = Employee.builder()
                .name(employee.getName())
                .address(employee.getAddress())
                .contact(employee.getContact())
                .idNumber(employee.getIdNumber())
                .department(employee.getDepartment())
                .sex(sex)
                .build();
        if (employee.getDepartment() != null && employee.getDepartment() != 0) {
            GlobalConstant.departmentNotExists.notNull(
                    departmentMapper.selectById(employee.getDepartment())
            );
            build.setDepartment(employee.getDepartment());
        }
        baseMapper.insert(build);
        // 现在的部门人数+1
        if (build.getDepartment() != null && build.getDepartment() != 0) {
            departmentMapper.updateAddCount(build.getDepartment());
        }
    }

    @Override
    public void updateEmployee(Employee employee) throws AssertException {
        Employee source = getById(employee.getId());
        GlobalConstant.employeeNotExist.notNull(
                source
        );
        GlobalConstant.employeeNameFormat.isTrue(
                employee.getName().matches(".{2,100}")
        );
        GlobalConstant.employeeAddressFormat.isTrue(
                employee.getAddress().length() < 300
        );
        GlobalConstant.employeeContactFormat.isTrue(
                employee.getContact().length() < 20
        );
        String validate = IdCardCheckUtil.IDCardValidate(employee.getIdNumber());
        if (validate != null) {
            throw new AssertException(validate);
        }
        String sex = IdCardCheckUtil.isMale(employee.getIdNumber()) ? "male" : "female";
        Employee build = Employee.builder()
                .id(employee.getId())
                .name(employee.getName())
                .address(employee.getAddress())
                .contact(employee.getContact())
                .idNumber(employee.getIdNumber())
                .sex(sex)
                .department(employee.getDepartment())
                .build();
        if (employee.getDepartment() != null && employee.getDepartment() != 0) {
            GlobalConstant.departmentNotExists.notNull(
                    departmentMapper.selectById(employee.getDepartment())
            );
            build.setDepartment(employee.getDepartment());
        }
        baseMapper.updateById(employee);
        // 原来的部门人数-1
        if (source.getDepartment() != 0) {
            departmentMapper.updateLessCount(source.getDepartment());
        }
        // 现在的部门人数+1
        if (build.getDepartment() != null && build.getDepartment() != 0) {
            departmentMapper.updateAddCount(build.getDepartment());
        }
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        Employee employee = getById(employeeId);
        GlobalConstant.employeeNotExist.notNull(employee);
        removeById(employeeId);
        // 原来的部门人数-1
        if (employee.getDepartment() != 0) {
            departmentMapper.updateLessCount(employeeId);
        }
    }

    @Override
    public Map<Integer, Department> departmentMap(List<Employee> employees) {
        HashMap<Integer, Department> map = new HashMap<>();
        Set<Integer> departmentIds = employees.stream()
                .map(Employee::getDepartment)
                .collect(Collectors.toSet());
        departmentIds.forEach(departmentId -> {
            map.put(departmentId, departmentMapper.selectById(departmentId));
        });
        return map;
    }

    @Override
    public void updatePwd(Integer id, String pwd) {
        GlobalConstant.employeePwdFormat.isTrue(
                pwd.matches("^[A-Za-z0-9]{1,16}")
        );
        baseMapper.updatePwdById(id, pwd);
    }

    @Override
    public String updateAddress(Integer id, String value) {
        GlobalConstant.employeeAddressFormat.isTrue(
                value.length() < 300
        );
        update(new UpdateWrapper<Employee>()
                .lambda()
                .set(Employee::getAddress, value)
                .eq(Employee::getId, id));
        return value;
    }

    @Override
    public String updateContact(Integer id, String value) {
        GlobalConstant.employeeContactFormat.isTrue(
                value.length() < 20
        );
        update(new UpdateWrapper<Employee>()
                .lambda()
                .set(Employee::getContact, value)
                .eq(Employee::getId, id));
        return value;
    }
}
