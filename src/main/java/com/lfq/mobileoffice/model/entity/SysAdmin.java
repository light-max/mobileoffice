package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统管理员表实体类
 */
@TableName("T_sys_admin")
@Data
public class SysAdmin {
    /*** 用户名 */
    @TableId
    private String username;

    /*** 密码 */
    private String pwd;
}
