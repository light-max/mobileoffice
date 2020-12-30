package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.EmployeeMapper;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.PagerData;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.data.response.NoticeRead_EmployeeName;
import com.lfq.mobileoffice.model.entity.Notice;
import com.lfq.mobileoffice.model.entity.NoticeRead;
import com.lfq.mobileoffice.service.NoticeService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 通知管理控制器
 *
 * @author 李凤强
 */
@Controller
public class NoticeController {

    @Resource
    NoticeService noticeService;

    @Resource
    EmployeeMapper employeeMapper;

    /**
     * 分页查询通知
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     */
    @GetMapping({"/admin/notice/list", "/admin/notice", "/admin/notice/list/{currentPage}"})
    @ViewModelParameter(key = "view", value = "notice")
    public String list(Model model, @PathVariable(required = false) Integer currentPage) {
        Page<Notice> page = noticeService.page(
                new Page<>(currentPage == null ? 1 : currentPage, 5),
                new QueryWrapper<Notice>().orderByDesc("create_time")
        );
        Notice top = noticeService.getOne(new QueryWrapper<Notice>().eq("top", true));
        List<Notice> notices = page.getRecords();
        Pager pager = new Pager(page, "/admin/notice/list/");
        if (top != null) {
            notices.add(top);
        }
        Map<Integer, Integer> readCountMap = noticeService.queryReadCountMap(notices);
        notices.remove(top);
        model.addAttribute("top", top);
        model.addAttribute("pager", pager);
        model.addAttribute("notices", notices);
        model.addAttribute("employeeCount", employeeMapper.selectCount(Wrappers.emptyWrapper()));
        model.addAttribute("readCountMap", readCountMap);
        return "/admin/notice/list";
    }

    /**
     * 分页查询通知
     *
     * @param currentPage 指定查询的页码，不指定时查询第一页
     */
    @GetMapping({"/notice/list", "/notice", "/notice/list/{currentPage}"})
    @ResponseBody
    public Response<PagerData> list(@PathVariable(required = false) Integer currentPage) {
        Page<Notice> page = noticeService.page(
                new Page<>(currentPage == null ? 1 : currentPage, 15),
                new QueryWrapper<Notice>().orderByDesc("top")
        );
        List<Notice> records = page.getRecords();
        Pager pager = new Pager(page, "/notice/list");
        return Response.pager(pager, records);
    }

    /**
     * 添加一条通知
     */
    @PostMapping("/admin/notice")
    @ResponseBody
    public Response<Notice> add(Notice notice) {
        noticeService.addNotice(notice);
        return Response.success(notice);
    }

    /**
     * 根据 noticeId 删除一条通知
     */
    @DeleteMapping("/admin/notice/{noticeId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable Integer noticeId) {
        noticeService.removeById(noticeId);
    }

    /**
     * 根据 noticeId 查询一条通知
     *
     * @throws AssertException 查不到就抛出{@link GlobalConstant#noticeNotExist}异常
     */
    @GetMapping({"/admin/notice/{noticeId}", "/notice/{noticeId}"})
    @ResponseBody
    public Response<Notice> getOne(@PathVariable Integer noticeId) {
        Notice notice = noticeService.getById(noticeId);
        GlobalConstant.noticeNotExist.notNull(notice);
        return Response.success(notice);
    }

    /**
     * 更新一条公告
     */
    @PutMapping("/admin/notice")
    @ResponseBody
    public Response<Notice> update(Notice notice) {
        noticeService.updateNotice(notice);
        return Response.success(notice);
    }

    /**
     * 分页查询某个公告中已读此公告的员工
     *
     * @return {@link NoticeRead_EmployeeName}
     */
    @GetMapping("/notice/read/{noticeId}")
    @ResponseBody
    public Response<PagerData> readEmployee(
            @RequestParam(value = "page", required = false) Integer currentPage,
            @PathVariable Integer noticeId
    ) {
        Page<NoticeRead> page = noticeService.readNoticePage(noticeId, currentPage);
        List<NoticeRead_EmployeeName> list = noticeService.cast(page.getRecords());
        return Response.pager(new Pager(page, "/notice/read/" + noticeId), list);
    }
}
