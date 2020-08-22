package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.entity.Room;

/**
 * 会议室管理服务
 */
public interface RoomService extends IService<Room> {
    /**
     * 添加会议室
     *
     * @param room
     * @throws AssertException
     */
    void addRoom(Room room) throws AssertException;

    /**
     * 修改会议室
     *
     * @param room
     * @throws AssertException
     */
    void updateRoom(Room room) throws AssertException;

    /**
     * 删除会议室，并且删除会议室的设备
     *
     * @param roomId
     */
    void deleteRoom(Integer roomId);
}
