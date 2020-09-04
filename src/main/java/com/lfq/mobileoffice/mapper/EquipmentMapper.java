package com.lfq.mobileoffice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfq.mobileoffice.model.entity.Equipment;
import org.springframework.stereotype.Repository;

/**
 * 设备表映射器
 *
 * @author 李凤强
 */
@Repository
public interface EquipmentMapper extends BaseMapper<Equipment> {
    /**
     * 更新设备名称和设备描述
     *
     * @param id   要更新的设备的ID
     * @param name
     * @param des
     */
    void updateNameAndDes(Integer id, String name, String des);
}
