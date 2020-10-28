package com.lfq.mobileoffice.model.data.response;

import com.lfq.mobileoffice.model.entity.BusinessTrip;
import com.lfq.mobileoffice.model.entity.Resource;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 出差表对象，附带{@link Resource}的对象
 *
 * @author 李凤强
 */
public class BusinessTrip_Resources extends BusinessTrip {

    /*** 附件资源文件 */
    private final List<Resource> resources;

    public BusinessTrip_Resources(BusinessTrip trip, @Nullable List<Resource> resources) {
        this.resources = resources == null ? new ArrayList<>() : resources;
        setId(trip.getId());
        setEmployeeId(trip.getEmployeeId());
        setDes(trip.getDes());
        setAddress(trip.getAddress());
        setStatus(trip.getStatus());
        setStart(trip.getStart());
        setEnd(trip.getEnd());
        setCreateTime(trip.getCreateTime());
    }

    public List<Resource> getResources() {
        return resources;
    }
}
