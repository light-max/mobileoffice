package com.lfq.mobileoffice.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.lfq.mobileoffice.util.datetranslate.DateParameter;
import com.lfq.mobileoffice.util.datetranslate.DateTranslate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员表实体
 *
 * @author 李凤强
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("T_admin")
public class Admin implements DateTranslate {
    /*** id */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /*** 用户名 */
    private String username;

    /*** 密码 */
    private String pwd;

    /*** 描述 */
    private String des;

    /*** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @DateParameter("YYYY年MM月dd日 HH:mm")
    private Long createTime;

    /*** 是否启用 */
    private Boolean enable;

    /**
     * 获取{@link #enable}字段的整数形式
     */
    public int getEnableToInt() {
        return enable ? 1 : 0;
    }
}
