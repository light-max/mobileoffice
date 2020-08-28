package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Room;
import com.lfq.mobileoffice.model.entity.RoomApply;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 会议室申请服务
 */
public interface RoomApplyService extends IService<RoomApply> {
    /**
     * 提交会议室预约申请
     *
     * @param employeeId 员工id
     * @param roomApply  包含了: 会议室id,开始时间与结束时间,附加消息
     * @throws AssertException {@link GlobalConstant#roomApplyTime},<br>
     *                         {@link GlobalConstant#roomApplyTime1},<br>
     *                         {@link GlobalConstant#roomApplyDesFormat},<br>
     *                         {@link GlobalConstant#roomApplyDuplicateTimePeriod}<br>
     */
    void postApply(Integer employeeId, RoomApply roomApply) throws AssertException;

    /**
     * 分页查询申请记录
     *
     * @param currentPage 当前页
     * @param status      要查询的状态
     * @return 预约申请记录, 按{@link RoomApply#getCreateTime()}降序排序
     */
    Page<RoomApply> listPage(@Nullable Integer currentPage, @Nullable Integer status);

    /**
     * 分页查询某个员工的预约申请记录
     * 如果是申请中的记录，直接返回全部，不做分页
     * 按道理来说某个员工的待受理记录肯定是寥寥无几的
     *
     * @param employeeId  员工id
     * @param currentPage 当前页
     * @param status      按状态查询
     * @return 这个员工的预约申请记录, 按{@link RoomApply#getCreateTime()}降序排序
     */
    Page<RoomApply> employeeListPage(Integer employeeId, @Nullable Integer currentPage, @Nullable Integer status);

    /**
     * 查询预约记录中所有出现的会议室, 并且以Map的形式保存
     *
     * @param list 预约记录
     * @return key:{@link Room#getId()}, value:{@link Room}
     */
    Map<Integer, Room> roomMap(List<RoomApply> list);

    /**
     * 查询预约记录中所有出现的员工, 并且以Map的形式保存
     *
     * @param list 预约记录
     * @return key:{@link Employee#getId()}, value:{@link Employee}
     */
    Map<Integer, Employee> employeeMap(List<RoomApply> list);

    /**
     * 修改预约申请的状态
     *
     * @param id     预约申请id
     * @param status 新状态, 参考{@link RoomApply#getStatus()}
     */
    void updateStatus(int id, int status);
}
