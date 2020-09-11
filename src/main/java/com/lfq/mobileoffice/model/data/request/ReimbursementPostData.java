package com.lfq.mobileoffice.model.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfq.mobileoffice.model.entity.BillItem;
import com.lfq.mobileoffice.model.entity.Reimbursement;
import lombok.Data;

import java.util.List;

/**
 * 添加报销单请求数据
 *
 * @author 李凤强
 */
@Data
public class ReimbursementPostData {

    /*** 对应{@link Reimbursement#getDes()} */
    private String des;

    /*** 对应{@link Reimbursement#getPayeeName()}*/
    private String payee;

    /*** 对应{@link Reimbursement#getBankCard()} */
    private String card;

    /*** 账单项目列表 */
    @JsonProperty("bills")
    private List<BillItem> billItems;

    /*** 资源文件数组 */
    private String[] resources;

}
