package com.lfq.mobileoffice.controller;

import com.lfq.mobileoffice.mapper.ResourceMapper;
import com.lfq.mobileoffice.model.entity.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * 资源文件控制器
 *
 * @author 李凤强
 */
@Controller
public class ResourceController {

    @javax.annotation.Resource
    ResourceMapper resourceMapper;

    /**
     * 员工请假文件上传路径
     */
    @Value("${mobileoffice.file-path.employee}")
    private String filePath;

    /**
     * 根据文件id查看文件
     */
    @GetMapping("/resource/{resourceId}")
    @ResponseBody
    public ResponseEntity<FileSystemResource> resource(@PathVariable String resourceId) {
        Resource resource = resourceMapper.selectById(resourceId);
        if (resource == null) {
            return ResponseEntity.status(404).build();
        }
        File path = new File(filePath, String.valueOf(resource.getEmployeeId()));
        File file = new File(path, resourceId);
        if (!file.exists()) {
            throw new RuntimeException("系统错误, 员工请假附件丢失\npath: " + file);
        }
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "filename=" + resource.getName());
//        headers.add("Content-Disposition", "attachment; filename=" + resource.getName());
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        headers.add("Last-Modified", new Date().toString());
//        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(resource.getSize())
                .contentType(MediaType.parseMediaType(resource.getType()))
                .body(new FileSystemResource(file));
    }
}
