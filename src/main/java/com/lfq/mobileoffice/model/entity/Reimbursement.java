package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * 报销表
 *
 * @author 李凤强
 */
@TableName("t_reimbursement")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reimbursement implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 员工id */
    private Integer employeeId;

    /*** 报销详情, 备注 */
    private String des;

    /*** 收款人姓名 */
    private String payeeName;

    /*** 收款人银行卡号 */
    private String bankCard;

    /**
     * 状态<ul>
     * <li>1.待审核</li>
     * <li>2.同意</li>
     * <li>3.拒绝</li>
     * </ul>
     */
    private Integer status;

    /*** 申请时间 */
    @TableField(fill = FieldFill.INSERT)
    @DateParameter("YYYY年MM月dd HH:mm")
    private Long createTime;

    /**
     * 把{@link #status}转换为文本, 供前端调用
     */
    public String getStatusText() {
        return new HashMap<Integer, String>() {{
            put(1, "<span class=\"text-primary\">待批准</span>");
            put(2, "<span class=\"text-success\">已同意</span>");
            put(3, "<span class=\"text-danger\">已拒绝</span>");
        }}.get(status);
    }
}
