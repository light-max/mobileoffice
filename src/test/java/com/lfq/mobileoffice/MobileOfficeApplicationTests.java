package com.lfq.mobileoffice;

import com.lfq.mobileoffice.mapper.AdminMapper;
import com.lfq.mobileoffice.model.entity.Admin;
import com.lfq.mobileoffice.model.entity.Department;
import com.lfq.mobileoffice.model.entity.Notice;
import com.lfq.mobileoffice.model.entity.Room;
import com.lfq.mobileoffice.service.DepartmentService;
import com.lfq.mobileoffice.service.NoticeService;
import com.lfq.mobileoffice.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Random;

@SpringBootTest
class MobileOfficeApplicationTests {
    @Resource
    AdminMapper adminMapper;

    @Resource
    NoticeService noticeService;

    @Resource
    DepartmentService departmentService;

    @Resource
    RoomService roomService;

    @Test
    void contextLoads() {
        for (int i = 0; i < 100; i++) {
            adminMapper.insert(Admin.builder()
                    .username("admin_test_" + i)
                    .des("")
                    .pwd("1234")
                    .build()
            );
        }
    }

    String rand(String text) {
        int max = new Random().nextInt(20);
        return text.repeat(Math.max(1, max));
    }

    @Test
    void addNotices() {
        for (int i = 0; i < 100; i++) {
            Notice notice = new Notice();
            notice.setTitle(rand("标题" + i));
            notice.setContent(rand("通知内容内容内容" + i));
            noticeService.addNotice(notice);
        }
    }

    @Test
    void addDepartment() {
        for (int i = 0; i < 100; i++) {
            departmentService.addDepartment(Department.builder()
                    .name(rand("部门" + i))
                    .des(rand("部门描述" + i))
                    .build()
            );
        }
    }

    @Test
    void addRooms() {
        for (int i = 0; i < 100; i++) {
            roomService.addRoom(Room.builder()
                    .name("会议室" + i)
                    .location(rand("地址" + i))
                    .capacity(new Random().nextInt(400) + 1)
                    .build()
            );
        }
    }
}
