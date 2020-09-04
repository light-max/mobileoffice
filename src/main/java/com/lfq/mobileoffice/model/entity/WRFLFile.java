package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请假条附件表
 *
 * @author 李凤强
 */
@Data
@TableName("T_wrfl_file")
@AllArgsConstructor
@NoArgsConstructor
public class WRFLFile {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 请假条id */
    private Long wrflId;

    /*** 资源id */
    private Long resourceId;
}
