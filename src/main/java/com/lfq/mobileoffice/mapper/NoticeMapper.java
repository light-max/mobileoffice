package com.lfq.mobileoffice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lfq.mobileoffice.model.entity.Notice;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 通知表映射器
 */
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {
    /**
     * 取消所有的置顶通知
     *
     * @param exceptId 这个id的通知除外
     */
    @Update("update T_notice set top=0 where id<>#{exceptId};")
    void unpin(Integer exceptId);
}
