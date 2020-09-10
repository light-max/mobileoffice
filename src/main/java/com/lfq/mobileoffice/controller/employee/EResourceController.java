package com.lfq.mobileoffice.controller.employee;

import com.lfq.mobileoffice.model.data.Response;
import com.lfq.mobileoffice.model.entity.Employee;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.ResourceService;
import com.lfq.mobileoffice.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 员工上传文件请求
 *
 * @author 李凤强
 */
@Controller
@RequestMapping("/employee/resource")
public class EResourceController {

    @javax.annotation.Resource
    ResourceService resourceService;

    /**
     * 上传
     */
    @PostMapping
    @ResponseBody
    @UseDefaultSuccessResponse
    public Response<Resource> post(
            @SessionAttribute("employee") Employee employee,
            MultipartFile file
    ) {
        Resource resource = resourceService.saveFile(employee.getId(), file);
        return Response.success(resource);
    }

    /**
     * 删除
     */
    @DeleteMapping("{resourceId}")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(@PathVariable("resourceId") String resourceId) {
        resourceService.deleteFile(resourceId);
    }

    /**
     * 删除多个
     */
    @DeleteMapping
    @ResponseBody
    @UseDefaultSuccessResponse
    public void delete(String[] ids) {
        for (String resource : ids) {
            resourceService.deleteFile(resource);
        }
    }
}
