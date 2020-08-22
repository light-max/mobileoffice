package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.EquipmentMapper;
import com.lfq.mobileoffice.mapper.RoomMapper;
import com.lfq.mobileoffice.model.entity.Equipment;
import com.lfq.mobileoffice.service.EquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Resource
    RoomMapper roomMapper;

    @Override
    public void addEquipment(Equipment equipment) throws AssertException {
        GlobalConstant.roomNotExists.notNull(
                roomMapper.selectById(equipment.getRoomId())
        );
        GlobalConstant.equipmentNameFormat.isTrue(
                equipment.getName().matches(".{1,30}")
        );
        GlobalConstant.equipmentDesFormat.isFalse(
                equipment.getDes().length() > 300
        );
        baseMapper.insert(equipment);
    }

    @Override
    public void updateEquipment(Equipment equipment) throws AssertException {
        GlobalConstant.equipmentNameFormat.isTrue(
                equipment.getName().matches(".{1,30}")
        );
        GlobalConstant.equipmentDesFormat.isFalse(
                equipment.getDes().length() > 300
        );
        baseMapper.updateNameAndDes(equipment.getId(), equipment.getName(), equipment.getDes());
    }
}
