package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.DepartmentMapper;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.mapper.TmpEmployeeMapper;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.TmpEmployee;
import com.lfq.mobileoffice.service.EmployeeService;
import com.lfq.mobileoffice.util.IdCardCheckUtil;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工管理服务
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    TmpEmployeeMapper tmpMapper;

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
            departmentMapper.updateLessCount(employee.getDepartment());
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

    @Override
    public XSSFWorkbook template(boolean department) {
        XSSFWorkbook book = new XSSFWorkbook();
        List<Department> list = department ?
                departmentMapper.selectList(Wrappers.emptyWrapper()) :
                Collections.singletonList(Department.builder().id(-1).build());
        for (Department d : list) {
            XSSFSheet sheet;
            if (d.getId() == -1) {
                sheet = book.createSheet("不分配部门的员工");
            } else {
                sheet = book.createSheet(d.getName());
            }
            XSSFRow ini = sheet.createRow(0);
            sheet.setDefaultColumnWidth(30);
            ini.createCell(0).setCellValue("注意：第一行数据请勿修改");
            ini.createCell(1).setCellValue("部门名称");
            ini.createCell(3).setCellValue("部门id");
            if (d.getId() != -1) {
                ini.createCell(2).setCellValue(d.getName());
                ini.createCell(4).setCellValue(String.valueOf(d.getId()));
            }
            int index = 0;
            XSSFRow thead = sheet.createRow(1);
            thead.createCell(index++).setCellValue("姓名(1至100个字符)");
            thead.createCell(index++).setCellValue("身份证号码(设置成字符格式)");
            thead.createCell(index++).setCellValue("联系方式(最多20个字)");
            thead.createCell(index).setCellValue("现居住地(最多300个字)");
        }
        return book;
    }

    @Override
    public void importEmployee(Integer adminId, MultipartFile file) {
        try {
            // 用来保存所有员工临时数据的容器
            List<TmpEmployee> employees = new ArrayList<>();
            // workbook对象
            Workbook sheets = WorkbookFactory.create(file.getInputStream());
            for (int i = 0; i < sheets.getNumberOfSheets(); i++) {
                Sheet sheet = sheets.getSheetAt(i);
                Row ini = sheet.getRow(0);
                if (ini == null) {
                    throw new AssertException(
                            GlobalConstant.templateIniRowNotExists.getMessage() +
                                    ",在工作簿\"" +
                                    sheet.getSheetName() + "\"的第一行没有发现内容"
                    );
                }
                Cell departmentNameCell = ini.getCell(2);
                Cell departmentIdCell = ini.getCell(4);
                // 当前待插入员工列表的部门id,为空就是不分配部门
                Integer departmentId = null;
                String departmentName = null;
                // 检测是否需要分配部门
                if (departmentIdCell != null) {
                    try {
                        departmentId = Integer.parseInt(departmentIdCell.getStringCellValue());
                        Department department = departmentMapper.selectById(departmentId);
                        if (department == null) {
                            throw new AssertException(
                                    "在工作簿\"" +
                                            sheet.getSheetName() + "\"的第1行的E列发生了错误:" +
                                            GlobalConstant.templateDepartmentNotExists.getMessage() +
                                            ",请删除此工作簿或重新下载模板"
                            );
                        }
                    } catch (NumberFormatException ignored) {
                        throw new AssertException(
                                "在工作簿\"" +
                                        sheet.getSheetName() + "\"的第1行的E列发生了:" +
                                        GlobalConstant.templateIniDepartmentIdFormat.getMessage() +
                                        "错误"
                        );
                    }
                }
                // 检查部门id与名称是否相符
                if (departmentId != null && departmentNameCell != null) {
                    try {
                        departmentName = departmentNameCell.getStringCellValue();
                        Department department = departmentMapper.selectById(departmentId);
                        GlobalConstant.templateIniDepartmentName.isTrue(
                                departmentName.equals(department.getName())
                        );
                    } catch (AssertException e) {
                        throw new AssertException(
                                "在工作簿\"" +
                                        sheet.getSheetName() + "\"的第1行的C列发生了错误:" +
                                        e.getMessage() +
                                        ",请删除此工作簿或重新下载模板"
                        );
                    } catch (Exception e) {
                        throw new AssertException(
                                "在工作簿\"" +
                                        sheet.getSheetName() + "\"的第1行的C列发生了错误:" +
                                        GlobalConstant.templateIniDepartmentNameFormat.getMessage() +
                                        ",请删除此工作簿或重新下载模板"
                        );
                    }
                }
                // 逐个获取员工信息
                for (int j = 2; j < sheet.getPhysicalNumberOfRows(); j++) {
                    int cellIndex = 0;
                    Row row = sheet.getRow(j);
                    TmpEmployee employee = TmpEmployee.builder()
                            .adminId(adminId)
                            .departmentId(departmentId)
                            .departmentName(departmentName)
                            .name(getCellString(row, cellIndex++))
                            .idNumber(getCellString(row, cellIndex++))
                            .contact(getCellString(row, cellIndex++))
                            .address(getCellString(row, cellIndex))
                            .build();
                    // 出现了断层，代表结尾了，什么都没有
                    if (employee.getName().isEmpty() &&
                            employee.getIdNumber().isEmpty() &&
                            employee.getContact().isEmpty() &&
                            employee.getAddress().isEmpty()
                    ) {
                        break;
                    }
                    // 检查员工信息是否符合要求
                    try {
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
                    } catch (AssertException e) {
                        throw new AssertException(
                                "在工作簿\"" +
                                        sheet.getSheetName() + "\"的第" +
                                        (j + 1) + "行中发生了错误: " + e.getMessage()
                        );
                    }
                    // 把员工数据放入容器中
                    employees.add(employee);
                }
            }
            // 把数据原来的数据删除掉
            LambdaUpdateWrapper<TmpEmployee> deleteWrapper = new UpdateWrapper<TmpEmployee>()
                    .lambda()
                    .eq(TmpEmployee::getAdminId, adminId);
            tmpMapper.delete(deleteWrapper);
            // 插入所有员工临时数据到数据库
            GlobalConstant.templateEmpty.isFalse(employees.isEmpty());
            for (TmpEmployee employee : employees) {
                tmpMapper.insert(employee);
            }
        } catch (IOException | EncryptedDocumentException e) {
            GlobalConstant.templateFileFormat.newException();
        }
    }

    /**
     * 获取列中某一个单元格的字符串内容, 如果不存在就返回空字符串，不抛出异常
     *
     * @param row   某一列
     * @param index 这一列的第几个
     */
    private String getCellString(Row row, int index) {
        try {
            return row.getCell(index).getStringCellValue();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public List<TmpEmployee> employeeTmp(Integer adminId) {
        LambdaQueryWrapper<TmpEmployee> wrapper = new QueryWrapper<TmpEmployee>()
                .lambda()
                .eq(TmpEmployee::getAdminId, adminId);
        return tmpMapper.selectList(wrapper);
    }

    @Override
    public void postEmployeeTmp(Integer adminId) {
        LambdaQueryWrapper<TmpEmployee> queryWrapper = new QueryWrapper<TmpEmployee>()
                .lambda()
                .eq(TmpEmployee::getAdminId, adminId);
        List<TmpEmployee> tmpEmployees = tmpMapper.selectList(queryWrapper);
        List<Employee> employees = new ArrayList<>();
        for (TmpEmployee tmp : tmpEmployees) {
            Employee employee = Employee.builder()
                    .name(tmp.getName())
                    .idNumber(tmp.getIdNumber())
                    .sex(IdCardCheckUtil.isMale(tmp.getIdNumber()) ? "male" : "female")
                    .address(tmp.getAddress())
                    .contact(tmp.getContact())
                    .department(tmp.getDepartmentId())
                    .build();
            employees.add(employee);
        }
        saveBatch(employees);
        List<Department> departments = departmentMapper.selectList(Wrappers.emptyWrapper());
        for (Department department : departments) {
            LambdaQueryWrapper<Employee> wrapper = new QueryWrapper<Employee>()
                    .lambda()
                    .eq(Employee::getDepartment, department.getId());
            department.setCount(baseMapper.selectCount(wrapper));
            departmentMapper.updateById(department);
        }
    }
}
