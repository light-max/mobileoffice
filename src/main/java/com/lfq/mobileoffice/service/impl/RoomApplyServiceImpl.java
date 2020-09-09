package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.mapper.RoomApplyMapper;
import com.lfq.mobileoffice.mapper.RoomMapper;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Room;
import com.lfq.mobileoffice.model.entity.RoomApply;
import com.lfq.mobileoffice.service.RoomApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomApplyServiceImpl extends ServiceImpl<RoomApplyMapper, RoomApply> implements RoomApplyService {

    @Resource
    RoomMapper roomMapper;

    @Resource
    EmployeeMapper employeeMapper;

    @Override
    public void postApply(Integer employeeId, RoomApply roomApply) throws AssertException {
        // 检查基本信息
        GlobalConstant.roomNotExists.notNull(
                roomMapper.selectById(roomApply.getRoomId())
        );
        GlobalConstant.roomApplyTime.isTrue(
                roomApply.getEnd() > roomApply.getStart()
        );
        GlobalConstant.roomApplyTime1.isTrue(
                roomApply.getStart() > System.currentTimeMillis()
        );
        GlobalConstant.roomApplyDesFormat.isTrue(
                roomApply.getDes().length() < 200
        );

        // 检查此事件端是否有人预约，已拒绝的预约除外
        LambdaQueryWrapper<RoomApply> wrapper = new QueryWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getRoomId, roomApply.getRoomId());
        Long start = roomApply.getStart();
        Long end = roomApply.getEnd();
        for (RoomApply apply : list(wrapper)) {
            // 检查时间段是否重复
            // 开始时间在已有记录的之间
            if (start >= apply.getStart() && start <= apply.getEnd()) {
                GlobalConstant.roomApplyDuplicateTimePeriod.newException();
            }
            // 结束时间在已有记录之间
            if (end >= apply.getEnd() && end <= apply.getEnd()) {
                GlobalConstant.roomApplyDuplicateTimePeriod.newException();
            }
            // 已有记录的开始时间在现在的时间之间
            if (apply.getStart() >= start && apply.getStart() <= end) {
                GlobalConstant.roomApplyDuplicateTimePeriod.newException();
            }
            // 已有记录的结束时间在现在的时间之间
            if (apply.getEnd() >= start && apply.getEnd() <= end) {
                GlobalConstant.roomApplyDuplicateTimePeriod.newException();
            }
        }

        // 插入数据
        RoomApply build = RoomApply.builder()
                .employeeId(employeeId)
                .roomId(roomApply.getRoomId())
                .start(roomApply.getStart())
                .end(roomApply.getEnd())
                .des(roomApply.getDes())
                .build();
        save(build);
    }

    @Override
    public Page<RoomApply> listPage(Integer currentPage, Integer status) {
        LambdaQueryWrapper<RoomApply> wrapper = new QueryWrapper<RoomApply>()
                .lambda()
                .orderByDesc(RoomApply::getCreateTime);
        if (status != null) {
            wrapper.eq(RoomApply::getStatus, status);
        }
        return page(new Page<>(currentPage == null ? 1 : currentPage, 15), wrapper);
    }

    @Override
    public Page<RoomApply> employeeListPage(Integer employeeId, Integer currentPage, Integer status) {
        LambdaQueryWrapper<RoomApply> wrapper = new QueryWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getEmployeeId, employeeId)
                .orderByDesc(RoomApply::getCreateTime);
        if (status != null) {
            wrapper.eq(RoomApply::getStatus, status);
            // 如果是申请中的记录，直接返回全部，不做分页
            // 按道理来说某个员工待受理的记录肯定是寥寥无几的
            if (status == 1) {
                // 2020年9月7日
                return page(new Page<>(1, Integer.MAX_VALUE), wrapper);
            }
        }
        return page(new Page<>(currentPage == null ? 1 : currentPage, 15), wrapper);
    }

    @Override
    public Map<Integer, Room> roomMap(List<RoomApply> list) {
        List<Integer> ids = list.stream()
                .map(RoomApply::getRoomId)
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        List<Room> rooms = roomMapper.selectBatchIds(ids);
        HashMap<Integer, Room> map = new HashMap<>();
        for (Room room : rooms) {
            map.put(room.getId(), room);
        }
        return map;
    }

    @Override
    public Map<Integer, Employee> employeeMap(List<RoomApply> list) {
        List<Integer> ids = list.stream()
                .map(RoomApply::getEmployeeId)
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        List<Employee> employees = employeeMapper.selectBatchIds(ids);
        HashMap<Integer, Employee> map = new HashMap<>();
        for (Employee employee : employees) {
            map.put(employee.getId(), employee);
        }
        return map;
    }

    @Override
    public void updateStatus(int id, int status) {
        LambdaUpdateWrapper<RoomApply> wrapper = new UpdateWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getId, id)
                .set(RoomApply::getStatus, status);
        update(wrapper);
    }

    @Override
    public void approveAll() {
        LambdaUpdateWrapper<RoomApply> wrapper = new UpdateWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getStatus, 1)
                .set(RoomApply::getStatus, 2);
        update(wrapper);
    }

    @Override
    public void refuseAll() {
        LambdaUpdateWrapper<RoomApply> wrapper = new UpdateWrapper<RoomApply>()
                .lambda()
                .eq(RoomApply::getStatus, 1)
                .set(RoomApply::getStatus, 3);
        update(wrapper);
    }
}
