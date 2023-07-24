package com.example.psmusic.service.impl;

import com.example.psmusic.dao.AdminMapper;
import com.example.psmusic.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 管理员service实现类
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 验证密码是否正确
     *
     * @param name
     * @param password
     * @return
     */
    @Override
    public boolean verifyPassword(String name, String password) {
        return adminMapper.verifyPassword(name, password) > 0;
    }
}
