package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签到时间表
 *
 * @author 李凤强
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("T_signin_time")
public class SignInTime implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 部门id */
    private Integer departmentId;

    /*** 在这个时间之前签到 */
    @TableField("time_before")
    private String start;

    /*** 在这个时间之后再签到 */
    @TableField("time_after")
    private String end;

    /*** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @DateParameter("YYYY.MM.dd HH:mm")
    private Long createTime;
}
