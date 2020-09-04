package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.Data;

/**
 * 请假条表
 *
 * @author 李凤强
 */
@Data
@TableName("T_wrfl")
public class WRFL implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 请假人id */
    private Integer employeeId;

    /*** 请假类型id */
    private Integer type;

    /*** 请假原因, 描述 */
    private String des;

    /**
     * 当前状态
     * <ul>
     *     <li>1:待审批</li>
     *     <li>2:拒绝</li>
     *     <li>3:同意</li>
     * </ul>
     */
    private Integer status;

    /*** 开始时间 */
    private Long start;

    /*** 结束时间 */
    private Long end;

    /*** 申请时间 */
    @DateParameter("YYYY年MM月dd日 HH:mm:")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
