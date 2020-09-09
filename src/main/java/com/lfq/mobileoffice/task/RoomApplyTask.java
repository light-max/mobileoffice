package com.lfq.mobileoffice.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lfq.mobileoffice.model.entity.Room;
import com.lfq.mobileoffice.model.entity.RoomApply;
import com.lfq.mobileoffice.service.RoomApplyService;
import com.lfq.mobileoffice.service.RoomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 会议室申请定时任务<br>
 * 检查员工成功预约的会议室的时间段<br>
 * 并且设置会议室状态与会议室申请状态
 * le: 小于等于
 * lt: 小于
 * ge: 大于等于
 * gt: 大于
 *
 * @author 李凤强
 */
@Component
public class RoomApplyTask implements CommandLineRunner {

    @Resource
    RoomApplyService roomApplyService;

    @Resource
    RoomService roomService;

    /**
     * 申请中的任务，如果过期了就设置成过期状态
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void task1() {
        // 预约开始时间小于等于当前时间就把这条预约记录设置成过期状态
        LambdaUpdateWrapper<RoomApply> wrapper = new UpdateWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getStatus, 1)
                .le(RoomApply::getStart, System.currentTimeMillis())
                .set(RoomApply::getStatus, 4);
        roomApplyService.update(wrapper);
//        System.out.println("task1: ");
    }

//    public static void main(String[] args) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        System.out.println(format.format(new Date(1598506800013L)));
//        System.out.println(format.format(new Date(1598506813746L)));
//        System.out.println(format.format(new Date(1598507450396L)));
//        System.out.println(format.format(new Date(1598507450396L)));
//        System.out.println(format.format(new Date(1598507760000L)));
//    }

    /**
     * 已同意的任务，如果同意了并且时间已经到了预约时间就把对应的会议室的{@link Room#getCurrentApplyId()}更新
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void task2() {
        // 查询预约开始时间小于等于当前时间的记录
        LambdaQueryWrapper<RoomApply> queryWrapper = new QueryWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getStatus, 2)
                .le(RoomApply::getStart, System.currentTimeMillis());
        for (RoomApply r : roomApplyService.list(queryWrapper)) {
            LambdaUpdateWrapper<Room> updateWrapper = new UpdateWrapper<Room>()
                    .lambda()
                    .eq(Room::getId, r.getRoomId())
                    .set(Room::getCurrentApplyId, r.getId());
            roomService.update(updateWrapper);
        }
//        System.out.println("task2");
    }

    /**
     * 检查正在使用的会议室，如果使用时间过了就修改{@link Room#getCurrentApplyId()}<br>
     * 检查正在使用的会议室: 检查{@link Room#getCurrentApplyId()}是否大于0
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void task3() {
        long millis = System.currentTimeMillis();
        // 当前申请id大于0
        LambdaQueryWrapper<Room> queryWrapper = new QueryWrapper<Room>()
                .lambda()
                .gt(Room::getCurrentApplyId, 0);
        for (Room room : roomService.list(queryWrapper)) {
            RoomApply one = roomApplyService.getById(room.getCurrentApplyId());
            // 如果当前时间大于预约结束时间就设置为空闲状态
            if (millis >= one.getEnd()) {
                LambdaUpdateWrapper<Room> updateWrapper = new UpdateWrapper<Room>()
                        .lambda()
                        .eq(Room::getId, room.getId())
                        .set(Room::getCurrentApplyId, 0);
                roomService.update(updateWrapper);
                // 并且设置这条记录为已被使用
                LambdaUpdateWrapper<RoomApply> wrapper = new UpdateWrapper<RoomApply>()
                        .lambda()
                        .eq(RoomApply::getId, one.getId())
                        .set(RoomApply::getStatus, 5);
                roomApplyService.update(wrapper);
            }
        }
//        System.out.println("task3");
    }

    // 测试
    public void task4() {
        LambdaQueryWrapper<Room> wrapper = new QueryWrapper<Room>()
                .lambda()
                .ge(Room::getId, 3)
                .le(Room::getId, 5);
        for (Room room : roomService.list(wrapper)) {
            System.out.println(room);
        }
    }

    // 测试
    public void task5() {
        LambdaUpdateWrapper<RoomApply> wrapper = new UpdateWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getStart, 1598306430122L)
                .le(RoomApply::getId, 83)
                .set(RoomApply::getId, 38);
        roomApplyService.update(wrapper);
    }

    // 测试
    public void task6() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        LambdaQueryWrapper<RoomApply> wrapper = new QueryWrapper<RoomApply>()
                .lambda()
//                .eq(RoomApply::getStatus, 1)
                ;
        for (RoomApply apply : roomApplyService.list(wrapper)) {
            System.out.print(format.format(new Date(apply.getStart())));
            System.out.print(" - ");
            System.out.print(format.format(new Date(apply.getEnd())));
            System.out.println("   ID: " + apply.getId());
        }
        System.out.println("当前时间: " + format.format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void run(String... args) throws Exception {
        task1();
        task2();
        task3();
    }
}
