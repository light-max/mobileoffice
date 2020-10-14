package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 从excel导入的员工信息的临时对象表
 *
 * @author 李凤强
 */
@Data
@Builder
@TableName("T_employee_tmp")
@AllArgsConstructor
@NoArgsConstructor
public class TmpEmployee {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 员工姓名 */
    private String name;

    /*** 员工身份证号码 */
    private String idNumber;

    /*** 员工现居住地 */
    private String address;

    /*** 员工联系方式 */
    private String contact;

    /*** 员工待分配部门的id */
    @Nullable
    private Integer departmentId;

    /*** 员工待分配部门名称 */
    @Nullable
    private String departmentName;

    /*** 做此操作的管理员的id */
    private Integer adminId;
}
