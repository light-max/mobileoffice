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
 * 请假条表
 *
 * @author 李凤强
 */
@Data
@TableName("T_wrfl")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WRFL implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 请假人id */
    private Integer employeeId;

    /*** 请假类型id */
    private Integer type;

    /*** 请假原因, 描述 */
    private String des;

    /**
     * 当前状态
     * <ul>
     *     <li>1:待审批</li>
     *     <li>2:同意</li>
     *     <li>3:拒绝</li>
     * </ul>
     * 默认为1
     */
    private Integer status;

    /*** 开始时间 */
    private Long start;

    /*** 结束时间 */
    private Long end;

    /*** 申请时间 */
    @DateParameter("YYYY年MM月dd HH:mm")
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 获取请假时间, 供前端调用
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
