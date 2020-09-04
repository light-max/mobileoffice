package com.lfq.mobileoffice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfq.mobileoffice.model.entity.Department;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 部门表映射器
 *
 * @author 李凤强
 */
@Repository
public interface DepartmentMapper extends BaseMapper<Department> {
    /**
     * 添加部门的员工人数
     *
     * @param id 部门id
     */
    @Update("update T_department set count=count+1 where id=#{id}")
    void updateAddCount(Integer id);

    /**
     * 减少部门的员工人数
     *
     * @param id 部门id
     */
    @Update("update T_department set count=count-1 where id=#{id}")
    void updateLessCount(Integer id);
}
