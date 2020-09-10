package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 出差申请表附件
 *
 * @author 李凤强
 */
@TableName("T_business_trip_file")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BTFile {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 出差申请表id */
    private Long btId;

    /*** 资源id */
    private String resourceId;
}
