package com.example.psmusic.service;

import com.example.psmusic.domain.Consumer;

import java.util.List;

/**
 * 前端用户service接口
 */
public interface ConsumerService {
    /**
     *增加
     */
    public boolean insert(Consumer consumer);

    /**
     *修改
     */
    public boolean update(Consumer consumer);

    /**
     * 删除
     */
    public boolean delete(Integer id);

    /**
     * 根据主键查询整个对象
     */
    public Consumer selectByPrimaryKey(Integer id);

    /**
     * 查询所有用户
     */
    public List<Consumer> allConsumer();

    /**
     * 验证密码是否正确
     */
    public boolean verifyPassword(String username,String password);

    /**
     * 根据账号查询
     */
    public Consumer getByUsername(String username);
}
