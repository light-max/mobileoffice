package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.entity.Equipment;

/**
 * 设备管理服务
 *
 * @author 李凤强
 */
public interface EquipmentService extends IService<Equipment> {
    /**
     * 添加设备
     *
     * @param equipment
     * @throws AssertException
     */
    void addEquipment(Equipment equipment) throws AssertException;

    /**
     * 更新设备信息
     *
     * @param equipment
     * @throws AssertException
     */
    void updateEquipment(Equipment equipment) throws AssertException;
}
