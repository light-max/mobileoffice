package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.entity.Department;

/**
 * 部门服务
 */
public interface DepartmentService extends IService<Department> {
    /**
     * 添加一个部门
     *
     * @param department
     * @throws AssertException
     */
    void addDepartment(Department department) throws AssertException;

    /**
     * 更新一条部门数据
     *
     * @param department
     * @throws AssertException
     */
    void updateDepartment(Department department) throws AssertException;
}
