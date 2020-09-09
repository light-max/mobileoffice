package com.lfq.mobileoffice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lfq.mobileoffice.model.entity.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源服务
 *
 * @author 李凤强
 */
public interface ResourceService extends IService<Resource> {
    /**
     * 保存文件的接口
     *
     * @param employeeId 员工id
     * @param file       文件对象
     */
    Resource saveFile(Integer employeeId, MultipartFile file);

    /**
     * 删除文件
     *
     * @param resourceId 文件id
     */
    void deleteFile(String resourceId);
}
