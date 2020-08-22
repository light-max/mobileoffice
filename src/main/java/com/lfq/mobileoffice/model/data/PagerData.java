package com.lfq.mobileoffice.model.data;

import lombok.Getter;

/**
 * 分页与数据
 */
@Getter
public class PagerData {
    /*** 分页 */
    private final Pager pager;

    /*** 数据 */
    private final Object data;

    public PagerData(Pager pager, Object data) {
        this.pager = pager;
        this.data = data;
    }
}
