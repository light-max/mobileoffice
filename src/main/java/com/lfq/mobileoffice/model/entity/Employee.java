package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工表实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("T_employee")
public class Employee implements DateTranslate {
    /*** id 员工工号 */
    @TableId(type = IdType.AUTO)
    private Integer id;

//    /*** 员工工号 */
//    private String jobId;

    /*** 员工姓名 */
    private String name;

    /*** 身份证号码 */
    private String idNumber;

    /*** 性别 */
    private String sex;

    /*** 居住地 */
    private String address;

    /*** 联系方式 */
    private String contact;

    /*** 部门id */
    private Integer department;

//    /*** 等级id */
//    private Integer grade;

    /*** 创建时间 */
    @DateParameter("YYYY/MM/dd")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    /*** 员工登陆密码 */
    private String pwd;
}
