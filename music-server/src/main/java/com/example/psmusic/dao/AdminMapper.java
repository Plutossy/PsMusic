package com.example.psmusic.dao;

import org.springframework.stereotype.Repository;

// 管理员Dao
@Repository
public interface AdminMapper {
    //    验证密码是否正确
    public int verifyPassword(String name, String password);
}
