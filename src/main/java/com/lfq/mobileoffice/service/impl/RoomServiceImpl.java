package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.EquipmentMapper;
import com.lfq.mobileoffice.mapper.RoomMapper;
import com.lfq.mobileoffice.model.entity.Equipment;
import com.lfq.mobileoffice.model.entity.Room;
import com.lfq.mobileoffice.service.RoomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Resource
    EquipmentMapper equipmentMapper;

    @Override
    public void addRoom(Room room) throws AssertException {
        GlobalConstant.roomNameFormat.isTrue(
                room.getName().matches(".{1,20}")
        );
        GlobalConstant.roomLocationFormat.isFalse(
                room.getLocation().length() > 200
        );
        GlobalConstant.roomCapacityFormat.notNull(
                room.getCapacity()
        );
        GlobalConstant.roomCapacityFormat.isTrue(
                room.getCapacity() >= 1 && room.getCapacity() <= 500
        );
        baseMapper.insert(Room.builder()
                .name(room.getName())
                .location(room.getLocation())
                .capacity(room.getCapacity())
                .build()
        );
    }

    @Override
    public void updateRoom(Room room) throws AssertException {
        GlobalConstant.roomNameFormat.isTrue(
                room.getName().matches(".{1,20}")
        );
        GlobalConstant.roomLocationFormat.isFalse(
                room.getLocation().length() > 200
        );
        GlobalConstant.roomCapacityFormat.notNull(
                room.getCapacity()
        );
        GlobalConstant.roomCapacityFormat.isTrue(
                room.getCapacity() >= 1 && room.getCapacity() <= 500
        );
        baseMapper.updateById(room);
    }

    @Override
    public void deleteRoom(Integer roomId) {
        baseMapper.deleteById(roomId);
        equipmentMapper.delete(new QueryWrapper<Equipment>().eq("room_id", roomId));
    }
}
