package com.lfq.mobileoffice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfq.mobileoffice.model.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 管理员表映射器
 *
 * @author 李凤强
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 更新管理员信息
     *
     * @param admin
     * @param enable
     */
    @Update("update T_admin set\n" +
            "username=#{a.username}," +
            "des=#{a.des}," +
            "enable=#{enable}\n" +
            "where id=#{a.id};")
    void updateInfo(@Param("a") Admin admin, Integer enable);

    /**
     * 更新密码
     *
     * @param id
     * @param pwd
     */
    @Update("update T_admin set pwd=#{pwd} where id=#{id};")
    void updatePwd(Integer id, String pwd);
}
