package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 账单项目表
 *
 * @author 李凤强
 */
@TableName("T_bill_item")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillItem {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 报销记录id */
    private Long reimbursementId;

    /*** 账单项目名称 */
    private String name;

    /*** 金额 */
    private BigDecimal amount;
}
