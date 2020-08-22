package com.lfq.mobileoffice.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.model.data.Pager;
import com.lfq.mobileoffice.model.data.PagerData;
import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Notice;
import com.lfq.mobileoffice.service.NoticeService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import com.lfq.mobileoffice.util.ump.ViewModelParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知管理控制器
 */
@Controller
public class NoticeController {

    @Resource
    NoticeService noticeService;

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
        model.addAttribute("top", top);
        model.addAttribute("pager", pager);
        model.addAttribute("notices", notices);
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
}
