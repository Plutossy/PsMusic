package com.example.psmusic.service;

import com.example.psmusic.domain.CommentUp;

/**
 * 评论点赞service接口
 */
public interface CommentUpService {
    /**
     *增加
     */
    public boolean insert(CommentUp commentUp);

    /**
     * 删除
     */
    public boolean delete(Integer userId, Integer commentId);

    /**
     * 根据主键查询整个对象
     */
    public CommentUp selectByPrimaryKey(Integer id);
}
