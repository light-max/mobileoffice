package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.model.data.response.NoticeRead_EmployeeName;
import com.lfq.mobileoffice.model.entity.Notice;
import com.lfq.mobileoffice.model.entity.NoticeRead;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 通知服务
 *
 * @author 李凤强
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 添加一条通知
     *
     * @param notice
     * @throws AssertException
     */
    void addNotice(Notice notice) throws AssertException;

    /**
     * 更新通知的内容
     *
     * @param notice
     * @throws AssertException
     */
    void updateNotice(Notice notice) throws AssertException;

    /**
     * 根据通知list查询这些通知的已读数量
     *
     * @param notices 通知列表
     * @return 返回一个map, key:{@link Notice#getId()}, value:已读数量
     */
    Map<Integer, Integer> queryReadCountMap(List<Notice> notices);

    /**
     * 分页查询已读某个通知的员工
     *
     * @param noticeId    通知id
     * @param currentPage 当前页
     * @return
     */
    Page<NoticeRead> readNoticePage(Integer noticeId, @Nullable Integer currentPage);

    /**
     * 把{@link NoticeRead}转换为{@link NoticeRead_EmployeeName}
     *
     * @param list 要转换的list
     * @return {@link NoticeRead_EmployeeName}
     */
    List<NoticeRead_EmployeeName> cast(List<NoticeRead> list);

    /**
     * 判断员工是否已读某条通知
     *
     * @param employeeId 员工id
     * @param noticeId   通知id
     * @return true:已读 false:未读
     */
    boolean isEmployeeReadNotice(int employeeId, int noticeId);

    /**
     * 员工已读某条通知
     *
     * @param employeeId 员工id
     * @param noticeId   通知id
     * @return true
     */
    boolean employeeReadNotice(int employeeId, int noticeId);
}
