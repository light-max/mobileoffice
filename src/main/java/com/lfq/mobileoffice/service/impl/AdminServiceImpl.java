package com.lfq.mobileoffice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfq.mobileoffice.constant.AssertException;
import com.lfq.mobileoffice.constant.GlobalConstant;
import com.lfq.mobileoffice.mapper.AdminMapper;
import com.lfq.mobileoffice.model.entity.Admin;
import com.lfq.mobileoffice.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public void addAdmin(Admin admin) throws AssertException {
        GlobalConstant.usernameFormat.isTrue(
                admin.getUsername().matches("^[A-Za-z0-9_]{4,16}")
        );
        GlobalConstant.passwordFormat.isTrue(
                admin.getPwd().matches("^[A-Za-z0-9_.]{4,16}")
        );
        GlobalConstant.desFormat.isTrue(
                admin.getDes().length() <= 120
        );
        GlobalConstant.usernameExists.isNull(baseMapper.selectOne(
                new QueryWrapper<Admin>().eq("username", admin.getUsername())
        ));
        baseMapper.insert(admin);
    }

    @Override
    public void updateInfo(Admin admin) throws AssertException {
        GlobalConstant.usernameFormat.isTrue(
                admin.getUsername().matches("^[A-Za-z0-9_]{4,16}")
        );
        if (baseMapper.selectById(admin.getId()).getUsername().equals(admin.getUsername())) {
            // 如果没有修改用户名就什么都不做
        } else {
            GlobalConstant.usernameExists.isNull(baseMapper.selectOne(
                    new QueryWrapper<Admin>().eq("username", admin.getUsername())
            ));
        }
        GlobalConstant.desFormat.isTrue(
                admin.getDes().length() <= 120
        );
        baseMapper.updateInfo(admin, admin.getEnableToInt());
    }

    @Override
    public void setPwd(Integer id, String[] pwd) throws AssertException {
        GlobalConstant.passwordsError.isTrue(
                pwd[1].equals(pwd[2])
        );
        GlobalConstant.passwordFormat.isTrue(
                pwd[1].matches("^[A-Za-z0-9_.]{4,16}")
        );
        GlobalConstant.sourcePasswordError.isTrue(
                baseMapper.selectById(id).getPwd().equals(pwd[0])
        );
        baseMapper.updatePwd(id, pwd[1]);
    }
}
