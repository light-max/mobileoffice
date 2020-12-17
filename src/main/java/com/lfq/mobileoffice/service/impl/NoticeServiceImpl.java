package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.mapper.NoticeMapper;
import com.lfq.mobileoffice.mapper.NoticeReadMapper;
import com.lfq.mobileoffice.model.data.response.NoticeRead_EmployeeName;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Notice;
import com.lfq.mobileoffice.model.entity.NoticeRead;
import com.lfq.mobileoffice.service.NoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Resource
    NoticeReadMapper noticeReadMapper;

    @Resource
    EmployeeMapper employeeMapper;

    @Override
    public void addNotice(Notice notice) throws AssertException {
        GlobalConstant.noticeTitleFormat.isTrue(
                notice.getTitle().matches(".{0,100}")
        );
        GlobalConstant.noticeContentFormat.isTrue(
                notice.getContent().matches(".{1,1000}")
        );
        baseMapper.insert(notice);
        if (notice.getTop() != null && notice.getTop()) {
            baseMapper.unpin(notice.getId());
        }
    }

    @Override
    public void updateNotice(Notice notice) throws AssertException {
        GlobalConstant.noticeTitleFormat.isTrue(
                notice.getTitle().matches(".{0,100}")
        );
        GlobalConstant.noticeContentFormat.isTrue(
                notice.getContent().matches(".{1,1000}")
        );
        // 只更新标题，内容，是否置顶这三个字段
        baseMapper.updateById(Notice.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .top(notice.getTop())
                .build()
        );
        if (notice.getTop() != null && notice.getTop()) {
            baseMapper.unpin(notice.getId());
        }
    }

    @Override
    public Map<Integer, Integer> queryReadCountMap(List<Notice> notices) {
        Map<Integer, Integer> map = new HashMap<>();
        for (Notice notice : notices) {
            LambdaQueryWrapper<NoticeRead> wrapper = new QueryWrapper<NoticeRead>()
                    .lambda()
                    .eq(NoticeRead::getNoticeId, notice.getId());
            Integer count = noticeReadMapper.selectCount(wrapper);
//            System.out.println(notice.getId() + ":" + count);
            map.put(notice.getId(), count);
        }
        return map;
    }

    @Override
    public Page<NoticeRead> readNoticePage(Integer noticeId, Integer currentPage) {
        LambdaQueryWrapper<NoticeRead> wrapper = new QueryWrapper<NoticeRead>()
                .lambda()
                .eq(NoticeRead::getNoticeId, noticeId);
        Page<NoticeRead> page = new Page<>(currentPage == null ? 1 : currentPage, 15);
        return noticeReadMapper.selectPage(page, wrapper);
    }

    @Override
    public List<NoticeRead_EmployeeName> cast(List<NoticeRead> list) {
        return new ArrayList<>() {{
            for (NoticeRead n : list) {
                Employee employee = employeeMapper.selectById(n.getEmployeeId());
                add(new NoticeRead_EmployeeName(
                        employee.getId(),
                        employee.getName(),
                        n.getCreateTime()
                ));
            }
        }};
    }

    @Override
    public boolean isEmployeeReadNotice(int employeeId, int noticeId) {
        LambdaQueryWrapper<NoticeRead> wrapper = new QueryWrapper<NoticeRead>()
                .lambda()
                .eq(NoticeRead::getEmployeeId, employeeId)
                .eq(NoticeRead::getNoticeId, noticeId);
        return noticeReadMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean employeeReadNotice(int employeeId, int noticeId) {
        // 如果是已读状态就直接返回
        if (isEmployeeReadNotice(employeeId, noticeId)) {
            return true;
        }
        NoticeRead build = NoticeRead.builder()
                .employeeId(employeeId)
                .noticeId(noticeId)
                .build();
        noticeReadMapper.insert(build);
        return true;
    }
}
