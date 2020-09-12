package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * 出差表
 * @author 李凤强
 */
@TableName("T_business_trip")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessTrip implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 员工id */
    private Integer employeeId;

    /*** 出差事由 */
    private String des;

    /*** 出差目标地址描述 */
    private String address;

    /**
     * 当前状态<ul>
     * <li>1.待审核</li>
     * <li>2.同意</li>
     * <li>3.拒绝</li>
     * </ul>
     */
    private Integer status;

    /*** 开始时间 */
    private Long start;

    /*** 结束时间 */
    private Long end;

    /*** 申请时间 */
    @TableField(fill = FieldFill.INSERT)
    @DateParameter("YYYY年MM月dd HH:mm")
    private Long createTime;

    /**
     * 获取出差时间, 供前端调用
     *
     * @return {@link #start}与{@link #end}转换后拼接在一起的字符串
     */
    @JsonIgnore
    public String getReservationTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String s = format.format(new Date(start));
        String e = format.format(new Date(end));
        return String.format("%s 至 %s", s, e);
    }

    /**
     * 把{@link #status}转换为文本, 供前端调用
     */
    @JsonIgnore
    public String getStatusText() {
        return new HashMap<Integer, String>() {{
            put(1, "<span class=\"text-primary\">待批准</span>");
            put(2, "<span class=\"text-success\">已同意</span>");
            put(3, "<span class=\"text-danger\">已拒绝</span>");
        }}.get(status);
    }
}
