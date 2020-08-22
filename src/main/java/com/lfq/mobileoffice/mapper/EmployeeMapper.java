package com.lfq.mobileoffice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfq.mobileoffice.model.entity.Employee;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 员工表映射器
 */
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * 根据员工id更新员工密码
     *
     * @param id
     * @param pwd
     */
    @Update("update T_employee set pwd=#{pwd} where id=#{id};")
    void updatePwdById(Integer id, String pwd);
}
