package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源文件表
 *
 * @author 李凤强
 */
@Data
@TableName("T_resource")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resource {
    /*** id,url,相对存储路径 */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /*** MediaType */
    private String type;

    /*** 资源名称 */
    private String name;

    /*** 资源大小 */
    private Long size;

    /*** 上传者的id */
    private Integer employeeId;

}
