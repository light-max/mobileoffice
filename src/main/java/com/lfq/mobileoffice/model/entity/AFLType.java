package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请假类型表
 *
 * @author 李凤强
 */
@Data
@TableName("T_afl_type")
@AllArgsConstructor
@NoArgsConstructor
public class AFLType {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /***类型名称 */
    private String name;
}
