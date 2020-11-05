package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知已读表
 *
 * @author 李凤强
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("T_notice_read")
public class NoticeRead implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /*** 通知id */
    private Integer noticeId;

    /*** 员工id */
    private Integer employeeId;

    /*** 创建时间，已读时间 */
    @DateParameter
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
}
