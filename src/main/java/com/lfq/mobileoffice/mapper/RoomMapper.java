package com.lfq.mobileoffice.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.model.entity.Room;
import jdk.jfr.Registered;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会议室表映射器
 *
 * @author 李凤强
 */
@Registered
public interface RoomMapper extends BaseMapper<Room> {

    /**
     * 按时间段查询可用的会议室
     *
     * @param wrapper
     * @param page
     * @param start   时间段的开始
     * @param end     时间段的结束
     */
    @Select("select T_room.id, name, location, capacity\n" +
            "from T_room\n" +
            "${ew.customSqlSegment} and (select count(1)\n" +
            "       from T_room_apply\n" +
            "       where T_room_apply.room_id = T_room.id\n" +
            "         and status in (1, 2)\n" +
            "         and (\n" +
            "               (#{start} > start and #{start} < end)\n" +
            "               or\n" +
            "               (#{end} > start and #{end} < end)\n" +
            "               or\n" +
            "               (start > #{start} and start < #{end})\n" +
            "               or\n" +
            "               (end > #{start} and end < #{end})\n" +
            "           )\n" +
            "      ) = 0 ")
    Page<Room> selectByTime(
            @Param(Constants.WRAPPER) Wrapper<Room> wrapper,
            Page<Room> page,
            long start, long end
    );

}
