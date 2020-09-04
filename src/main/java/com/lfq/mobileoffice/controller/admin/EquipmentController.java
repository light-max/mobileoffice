package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Equipment;
import com.lfq.mobileoffice.service.EquipmentService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会议室设备控制器
 *
 * @author 李凤强
 */
@Controller
public class EquipmentController {

    @Resource
    EquipmentService equipmentService;

    /**
     * 查询会议室所有设备
     */
    @GetMapping({"/admin/equipment/list", "/equipment/list"})
    @ResponseBody
    public Response<List<Equipment>> all(
            @RequestParam(value = "roomId", required = false) Integer roomId
    ) {
        if (roomId == null) {
            return Response.success(equipmentService.list());
        } else {
            LambdaQueryWrapper<Equipment> wrapper = new QueryWrapper<Equipment>()
                    .lambda()
                    .eq(Equipment::getRoomId, roomId);
            return Response.success(equipmentService.list(wrapper));
        }
    }

    /**
     * 查询单个会议室设备
     */
    @GetMapping({"/admin/equipment/{equipmentId}", "/equipment/{equipmentId}"})
    @ResponseBody
    public Response<Equipment> getOne(@PathVariable Integer equipmentId) {
        Equipment equipment = equipmentService.getById(equipmentId);
        GlobalConstant.equipmentNotExists.notNull(equipment);
        return Response.success(equipment);
    }

    /**
     * 添加会议室的设备
     */
    @PostMapping("/admin/equipment")
    @ResponseBody
    public Response<Equipment> add(Equipment equipment) {
        equipmentService.addEquipment(equipment);
        return Response.success(equipment);
    }

    /**
     * 更新会议室设备
     */
    @PutMapping("/admin/equipment")
    @ResponseBody
    public Response<Equipment> update(Equipment equipment) {
        equipmentService.updateEquipment(equipment);
        return Response.success(equipment);
    }

    /**
     * 删除会议室设备
     */
    @DeleteMapping("/admin/equipment/{equipmentId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer equipmentId) {
        equipmentService.removeById(equipmentId);
    }
}
