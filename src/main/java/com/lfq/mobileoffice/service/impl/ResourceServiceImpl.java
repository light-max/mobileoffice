package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.ResourceMapper;
import com.lfq.mobileoffice.model.entity.Resource;
import com.lfq.mobileoffice.service.ResourceService;
import com.lfq.mobileoffice.util.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Value("${mobileoffice.file-path.employee}")
    private String filePath;

    @Override
    public Resource saveFile(Integer employeeId, MultipartFile file) {
        Resource resource = Resource.builder()
                .employeeId(employeeId)
                .type(file.getContentType())
                .name(new File(Objects.requireNonNull(file.getOriginalFilename())).getName())
                .size(file.getSize())
                .build();
        save(resource);
        if (!IOUtil.writeEmployeeFile(filePath, employeeId, resource.getId(), file)) {
            removeById(resource.getId());
            GlobalConstant.error.newException();
        }
        return resource;
    }

    @Override
    public void deleteFile(String resourceId) {
        Resource resource = getById(resourceId);
        File path = new File(filePath, String.valueOf(resource.getEmployeeId()));
        new File(path, resourceId).delete();
        removeById(resourceId);
    }

}
