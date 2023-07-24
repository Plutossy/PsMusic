package com.example.psmusic.service;

// 管理员Service接口
public interface AdminService {
    //    验证密码是否正确
    public boolean verifyPassword(String username, String password);
}
