package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.PagerData;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Room;
import com.lfq.mobileoffice.service.RoomService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会议室管理控制器
 *
 * @author 李凤强
 */
@Controller
public class RoomController {

    @Resource
    RoomService roomService;

    /**
     * 分页查询会议室
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     */
    @GetMapping(value = {"/admin/room/list", "/admin/room", "/admin/room/list/{currentPage}"},
            produces = "text/html")
    @ViewModelParameter(key = "view", value = "room")
    public String list(Model model, @PathVariable(required = false) Integer currentPage) {
        Page<Room> page = roomService.page(new Page<>(currentPage == null ? 1 : currentPage, 10));
        List<Room> rooms = page.getRecords();
        Pager pager = new Pager(page, "/admin/room/list/");
        model.addAttribute("pager", pager);
        model.addAttribute("rooms", rooms);
        return "/admin/room/list";
    }

    /**
     * 分页查询会议室可以附带查询条件
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     * @param name        按名称模糊查询
     * @param capacity    可容纳人数
     * @param start       时间段
     * @param end         时间段
     */
    @GetMapping({"/room/list", "/room/list/{currentPage}"})
    @ResponseBody
    public Response<PagerData> list(
            @PathVariable(required = false) Integer currentPage,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Long start,
            @RequestParam(required = false) Long end
    ) {
        Page<Room> page = roomService.query(currentPage, name, capacity, start, end);
        List<Room> rooms = page.getRecords();
        Pager pager = new Pager(page, "/room/list/");
        return Response.pager(pager, rooms);
    }

    /**
     * 查询某个会议室
     */
    @GetMapping({"/admin/room/{roomId}", "/room/{roomId}"})
    @ResponseBody
    public Response<Room> getOne(@PathVariable Integer roomId) {
        Room room = roomService.getById(roomId);
        GlobalConstant.roomNotExists.notNull(room);
        return Response.success(room);
    }

    /**
     * 添加会议室
     */
    @PostMapping("/admin/room")
    @ResponseBody
    public Response<Room> add(Room room) {
        roomService.addRoom(room);
        return Response.success(room);
    }

    /**
     * 修改会议室
     */
    @PutMapping("/admin/room")
    @ResponseBody
    public Response<Room> update(Room room) {
        roomService.updateRoom(room);
        return Response.success(room);
    }

    /**
     * 删除会议室
     */
    @DeleteMapping("/admin/room/{roomId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer roomId) {
        roomService.deleteRoom(roomId);
    }
}
