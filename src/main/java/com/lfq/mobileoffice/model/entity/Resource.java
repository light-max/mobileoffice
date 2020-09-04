package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
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
public class Resource {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** MediaType */
    private String type;

    /*** 资源名称 */
    private String name;

    /*** 存储路径, 相对路径, 使用md5计算出来 */
    private String location;

    /*** 资源大小 */
    private Long size;
}
