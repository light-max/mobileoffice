package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 会议室申请记录
 *
 * @author 李凤强
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("T_room_apply")
public class RoomApply implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 目标会议室 */
    private Integer roomId;

    /*** 员工id */
    private Integer employeeId;

    /*** 开始时间 */
    private Long start;

    /*** 结束时间 */
    private Long end;

    /**
     * 当前状态<ul>
     * <li>1,申请中</li>
     * <li>2,同意</li>
     * <li>3,拒绝</li>
     * <li>4,已过期</li>
     * <li>5,已被使用</li></ul>
     */
    private Integer status;

    /*** 备注 */
    private String des;

    /*** 申请发起时间 */
    @DateParameter
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 获取预约时间, 供前端调用
     *
     * @return {@link #start}与{@link #end}转换后拼接在一起的字符串
     */
    public String getReservationTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String s = format.format(new Date(start));
        String e = format.format(new Date(end));
        return String.format("%s 至 %s", s, e);
    }

    /**
     * 把{@link #status}转换为文本, 供前端调用
     */
    public String getStatusText() {
        return new HashMap<Integer, String>() {{
            put(1, "<span class=\"text-primary\">待批准</span>");
            put(2, "<span class=\"text-success\">已同意</span>");
            put(3, "<span class=\"text-danger\">已拒绝</span>");
            put(4, "<span class=\"text-secondary\">已过期</span>");
            put(5, "<span class=\"text-dark\">已使用</span>");
        }}.get(status);
    }
}
