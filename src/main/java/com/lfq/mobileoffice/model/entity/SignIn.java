package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签到表
 *
 * @author 李凤强
 */
@Data
@Builder
@TableName("T_signin")
@AllArgsConstructor
@NoArgsConstructor
public class SignIn implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 员工id */
    private Integer employeeId;

    /*** 签到规则id */
    @TableField("signin_time_id")
    private Integer signInTimeId;

    /**
     * 签到类型
     * <ul>
     *     <li>1.上班</li>
     *     <li>2.下班</li>
     * </ul>
     */
    private Integer type;

    /*** 签到时间 */
    @TableField(fill = FieldFill.INSERT)
    @DateParameter("YYYY.MM.dd HH:mm:ss")
    private Long createTime;
}
