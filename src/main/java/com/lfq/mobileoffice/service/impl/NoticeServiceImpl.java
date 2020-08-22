package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.NoticeMapper;
import com.lfq.mobileoffice.model.entity.Notice;
import com.lfq.mobileoffice.service.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
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
}
