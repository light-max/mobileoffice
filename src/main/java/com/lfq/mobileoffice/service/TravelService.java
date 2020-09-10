package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.model.entity.BusinessTrip;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 出差服务
 *
 * @author 李凤强
 */
public interface TravelService extends IService<BusinessTrip> {
    /**
     * 提交一个出差申请
     *
     * @param employeeId 出差员工id
     * @param trip       出差申请表信息
     * @param resources  出差申请表附件
     */
    void postTravel(Integer employeeId, BusinessTrip trip, String[] resources);

    /**
     * 分页查询出差申请
     *
     * @param currentPage 当前页
     * @param status      状态
     * @return
     */
    Page<BusinessTrip> listPage(Integer currentPage, Integer status);

    /**
     * 查询出差申请中所有出现的员工
     *
     * @param trips 保存了出差申请的集合
     * @return key:{@link BusinessTrip#getId()}, value:{@link BusinessTrip}
     */
    Map<Integer, Employee> employeeMap(List<BusinessTrip> trips);

    /**
     * 按出差表id查询所带附件
     *
     * @param travelId 出差id
     * @return 如果没有附件就返回null
     */
    @Nullable
    List<Resource> getResources(Long travelId);

    /**
     * 同意一条出差的请假申请
     *
     * @param btId 出差id
     */
    void approve(Long btId);

    /**
     * 拒绝一条出差申请
     *
     * @param btId 出差id
     */
    void refuse(Long btId);

    /**
     * 同意所有出差申请
     */
    void approveAll();

    /**
     * 拒绝所有出差申请
     */
    void refuseAll();


}
