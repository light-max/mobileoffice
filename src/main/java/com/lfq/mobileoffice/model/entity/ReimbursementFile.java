package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账单附件表
 *
 * @author 李凤强
 */
@TableName("T_reimbursement_file")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReimbursementFile {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 报销单id */
    private Long reimbursementId;

    /*** 资源id */
    private String resourceId;
}
