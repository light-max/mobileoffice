package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通知表的实体类
 */
@TableName("T_notice")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 通知标题 */
    private String title;

    /*** 通知内容 */
    private String content;

    /*** 通知的图片数量 */
    private Integer imageCount;

    /*** 通知的创建时间 */
    @DateParameter
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    /*** 通知是否置顶 */
    private Boolean top;
}
