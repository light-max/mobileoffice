package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会议室表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("T_room")
public class Room {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 会议室名称 */
    private String name;

    /*** 会议室地址 */
    private String location;

    /*** 会议室可容纳人数 */
    private Integer capacity;
}
