package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门表实体类
 *
 * @author 李凤强
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("T_department")
public class Department implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 部门名称 */
    private String name;

    /*** 部门描述 */
    private String des;

    /*** 部门人数 */
    private Integer count;

    /*** 创建时间 */
    @DateParameter("YYYY年MM月dd日")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
