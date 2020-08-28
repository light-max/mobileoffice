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

    /**
     * 当前是否正在被使用
     * 0  : 当前处于空闲状态
     * 其他: 代表申请记录的ID,可以通过这个ID查询到申请者,起始时间和结束时间
     */
    private Long currentApplyId;
}
