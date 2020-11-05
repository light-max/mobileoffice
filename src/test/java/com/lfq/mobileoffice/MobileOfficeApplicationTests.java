package com.lfq.mobileoffice;

import com.lfq.mobileoffice.mapper.AdminMapper;
import com.lfq.mobileoffice.mapper.NoticeReadMapper;
import com.lfq.mobileoffice.model.entity.*;
import com.lfq.mobileoffice.service.*;
import com.lfq.mobileoffice.util.IdCardCreateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    @Resource
    EmployeeService employeeService;

    @Resource
    EquipmentService equipmentService;

    @Resource
    NoticeReadMapper noticeReadMapper;

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

    @Test
    void addEmployee() throws Exception {
        String path = "E:\\IDEA\\biyesheji\\mobileoffice\\demo.xlsx";
        Workbook book = WorkbookFactory.create(new File(path));
        Sheet sheet = book.getSheet("Sheet1");
        int max = sheet.getLastRowNum() + 1;
        for (int i = 0; i < max; i++) {
            Row row = sheet.getRow(i);
            Employee employee = Employee.builder()
                    .idNumber(IdCardCreateUtil.getIdNo(row.getCell(2) == null))
                    .name(String.valueOf(row.getCell(1)))
                    .address("随机" + new Random().nextInt(10000))
                    .contact("随机" + new Random().nextInt(10000))
                    .build();
            employeeService.addEmployee(employee);
        }
    }

    @Test
    void setDepartment() {
        Random random = new Random();
        List<Department> departments = departmentService.list();
        for (Employee employee : employeeService.list()) {
            int i = random.nextInt(departments.size());
            employee.setDepartment(departments.get(i).getId());
            employeeService.updateEmployee(employee);
        }
    }

    @Test
    void addDepartmentEquipment() {
        List<String> list = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        for (Room room : roomService.list()) {
            Collections.shuffle(list);
            StringBuilder password = new StringBuilder("密码：");
            for (String s : list) {
                password.append(s);
            }
            Equipment wifi = Equipment.builder()
                    .name("无线网络wifi")
                    .des(password.toString())
                    .roomId(room.getId())
                    .build();
            Equipment projector = Equipment.builder()
                    .name("投影仪")
                    .des("这是会议室\"" + room.getName() + "\"的投影仪")
                    .roomId(room.getId())
                    .build();
            equipmentService.addEquipment(wifi);
            equipmentService.addEquipment(projector);
        }
    }

    @Test
    void addNotice() {
        long towYear = 1000L * 60 * 60 * 24 * 365 * 2;
        long millis = System.currentTimeMillis();
        Random r = new Random();
        String[][] a = {
                {"加班通知", "因工作需要，今晚都要加班"},
                {"放假通知", "本周六不加班，全体放假"},
                {"团建通知", "这个月月末团建，请大家做好准备"}
        };
        for (int i = 0; i < 50; i++) {
            String[] s = a[r.nextInt(a.length)];
            StringBuilder content = new StringBuilder();
            int count = r.nextInt(10) + 5;
            for (int j = 0; j < count; j++) {
                content.append(s[1]);
            }
            long createTime = millis - (r.nextLong() % towYear);
            Notice notice = Notice.builder()
                    .title(s[0])
                    .content(content.toString())
                    .createTime(createTime)
                    .build();
            noticeService.addNotice(notice);
        }
    }

    @Test
    public void insertNoticeReader() {
        for (Employee employee : employeeService.list()) {
            NoticeRead build = NoticeRead.builder()
                    .noticeId(315)
                    .employeeId(employee.getId())
                    .build();
            noticeReadMapper.insert(build);
        }
    }
}
