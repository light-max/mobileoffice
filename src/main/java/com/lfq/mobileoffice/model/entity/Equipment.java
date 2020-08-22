package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会议室设备表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("T_equipment")
public class Equipment {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 所属会议室 */
    private Integer roomId;

    /*** 设备名称 */
    private String name;

    /*** 设备描述 */
    private String des;
}
