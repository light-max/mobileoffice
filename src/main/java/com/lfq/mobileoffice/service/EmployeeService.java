package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.TmpEmployee;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 员工管理服务
 *
 * @author 李凤强
 */
public interface EmployeeService extends IService<Employee> {
    /**
     * 分页查询员工列表，并且可以按部门查询
     *
     * @param currentPage 当前页
     * @param department  部门id
     * @return
     */
    Page<Employee> listPage(@Nullable Integer currentPage, @Nullable Integer department);

    /**
     * 添加员工
     *
     * @param employee
     * @throws AssertException
     */
    void addEmployee(Employee employee) throws AssertException;

    /**
     * 修改员工信息
     *
     * @param employee
     * @throws AssertException
     */
    void updateEmployee(Employee employee) throws AssertException;

    /**
     * 删除员工
     *
     * @param employeeId
     */
    void deleteEmployee(Integer employeeId);

    /**
     * 查询员工列表中所有部门，并以map的形式返回
     *
     * @param employees
     * @return map.key:{@link Department#getId()}|{@link Employee#getDepartment()}<br>
     * map.value:{@link Department}
     */
    Map<Integer, Department> departmentMap(List<Employee> employees);

    /**
     * 更新密码
     *
     * @param id  员工id
     * @param pwd 新密码
     */
    void updatePwd(Integer id, String pwd);

    /**
     * 根据员工id修改员工现居住地
     *
     * @param id    员工id
     * @param value 新的值
     * @return 新的居住地
     */
    String updateAddress(Integer id, String value);

    /**
     * 根据id修改员工联系方式
     *
     * @param id    员工id
     * @param value 新的值
     * @return 新的联系方式
     */
    String updateContact(Integer id, String value);

    /**
     * 生成批量导入员工excel模板
     *
     * @param department 是否按部门生成
     * @return xlsx格式的excel对象
     */
    XSSFWorkbook template(boolean department);

    /**
     * 批量导入员工
     *
     * @param adminId 当前管理员id
     * @param file    这是由前端上传的excel文件
     */
    void importEmployee(Integer adminId, MultipartFile file);

    /**
     * 根据管理员id查询批量导入后待添加的员工
     *
     * @param adminId 管理员id
     * @return
     */
    List<TmpEmployee> employeeTmp(Integer adminId);

    /**
     * 把批量导入的员工插入到员工表
     *
     * @param adminId 管理员id
     */
    void postEmployeeTmp(Integer adminId);
}
