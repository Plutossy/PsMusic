package com.example.psmusic.dao;

import com.example.psmusic.domain.CommentUp;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentUpMapper {
    /**
     * 增加
     */
    public int insert(CommentUp commentUp);

    /**
     * 删除
     */
    public int delete(Integer userId, Integer commentId);

    /**
     * 根据主键查询整个对象
     */
    public CommentUp selectByPrimaryKey(Integer id);
}
