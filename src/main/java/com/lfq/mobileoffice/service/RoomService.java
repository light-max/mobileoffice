package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.entity.Room;

/**
 * 会议室管理服务
 *
 * @author 李凤强
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

    /**
     * 分页查询会议室可以附带查询条件
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     * @param name        按名称模糊查询
     * @param capacity    可容纳人数
     * @param start       时间段
     * @param end         时间段
     */
    Page<Room> query(
            Integer currentPage,
            String name,
            Integer capacity,
            Long start, Long end
    );
}
