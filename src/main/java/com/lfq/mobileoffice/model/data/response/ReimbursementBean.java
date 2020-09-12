package com.lfq.mobileoffice.model.data.response;

import com.lfq.mobileoffice.model.entity.BillItem;
import com.lfq.mobileoffice.model.entity.Reimbursement;
import com.lfq.mobileoffice.model.entity.Resource;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 附带{@link Resource}与{@link BillItem}的报销记录对象
 */
public class ReimbursementBean extends Reimbursement {

    /*** 资源文件 */
    private final List<Resource> resources;

    /*** 账单项目 */
    private final List<BillItem> billItems;

    /*** 总金额 */
    private Float total = 0f;

    public ReimbursementBean(Reimbursement o, @Nullable List<Resource> resources, @Nullable List<BillItem> billItems) {
        this.resources = resources == null ? new ArrayList<>() : resources;
        this.billItems = billItems == null ? new ArrayList<>() : billItems;
        if (billItems != null) {
            for (BillItem item : billItems) {
                total += item.getAmount().floatValue();
            }
        }
        setId(o.getId());
        setEmployeeId(o.getEmployeeId());
        setDes(o.getDes());
        setPayeeName(o.getPayeeName());
        setBankCard(o.getBankCard());
        setStatus(o.getStatus());
        setCreateTime(o.getCreateTime());
    }

    public List<Resource> getResources() {
        return resources;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public Float getTotal() {
        return total;
    }
}
