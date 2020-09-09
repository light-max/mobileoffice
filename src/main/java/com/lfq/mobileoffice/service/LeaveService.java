package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.model.entity.AFLType;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.model.entity.WRFL;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 请假服务
 *
 * @author 李凤强
 */
public interface LeaveService extends IService<WRFL> {
    /**
     * 员工提交请假申请
     *
     * @param employeeId 员工id
     * @param wrfl       申请信息，包含了<ul>
     *                   <li>{@link WRFL#getType()}</li>
     *                   <li>{@link WRFL#getDes()}</li>
     *                   <li>{@link WRFL#getStart()}</li>
     *                   <li>{@link WRFL#getEnd()}</li></ul>
     * @param resources  请假所带附件的资源id
     */
    void postLeaveRequest(Integer employeeId, WRFL wrfl, String[] resources);

    /**
     * 按状态分页查询所有请假记录
     *
     * @param currentPage 当前页
     * @param status      状态
     * @return
     */
    Page<WRFL> listPage(@Nullable Integer currentPage, @Nullable Integer status);

    /**
     * 查询请假申请中所有出现的员工
     *
     * @param wrfls 保存了请假申请记录的集合
     * @return key:{@link WRFL#getEmployeeId()},value:{@link Employee}
     */
    Map<Integer, Employee> employeeMap(List<WRFL> wrfls);

    /**
     * 查询所有请假类型, 以map的形式返回
     *
     * @return key:{@link AFLType#getId()},value:{@link AFLType#getName()}
     */
    Map<Integer, String> typesMap();

    /**
     * 根据请假条id获取请假条所带的附件
     *
     * @param wrflId 请假条id
     * @return
     */
    List<Resource> getResources(Long wrflId);

    /**
     * 同意一条请假条的请假申请
     *
     * @param wrflId 请假条id
     */
    void approve(Long wrflId);

    /**
     * 拒绝一条请假条申请
     *
     * @param wrflId 请假条id
     */
    void refuse(Long wrflId);

    /**
     * 同意所有请假申请
     */
    void approveAll();

    /**
     * 拒绝所有请假申请
     */
    void refuseAll();

    /**
     * 添加请假类型
     *
     * @param name 类型名
     */
    AFLType addType(String name);

    /**
     * 删除请假类型
     *
     * @param typeId 类型id
     */
    void deleteType(Integer typeId);
}
