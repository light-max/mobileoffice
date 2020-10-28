package com.lfq.mobileoffice.model.data.response;

import com.lfq.mobileoffice.model.entity.AFLType;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.model.entity.WRFL;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 请假表对象，附带{@link AFLType#getName()}与{@link List<Resource>}的对象
 *
 * @author 李凤强
 */
public class WRFL_TypeName extends WRFL {

    /*** {@link AFLType#getName()} */
    private final String typeName;

    /*** 附件资源文件 */
    private final List<Resource> resources;

    public WRFL_TypeName(WRFL wrfl, String typeName, @Nullable List<Resource> resources) {
        this.typeName = typeName;
        this.resources = resources == null ? new ArrayList<>() : resources;
        setId(wrfl.getId());
        setEmployeeId(wrfl.getEmployeeId());
        setType(wrfl.getType());
        setDes(wrfl.getDes());
        setStatus(wrfl.getStatus());
        setStart(wrfl.getStart());
        setEnd(wrfl.getEnd());
        setCreateTime(wrfl.getCreateTime());
    }

    public String getTypeName() {
        return typeName;
    }

    public List<Resource> getResources() {
        return resources;
    }
}
