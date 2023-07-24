package com.example.psmusic.service.impl;

import com.example.psmusic.dao.CommentUpMapper;
import com.example.psmusic.domain.CommentUp;
import com.example.psmusic.service.CommentUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评论service实现类
 */
@Service
public class CommentUpServiceImpl implements CommentUpService {

    @Autowired
    private CommentUpMapper commentUpMapper;
    /**
     * 增加
     *
     * @param commentUp
     */
    @Override
    public boolean insert(CommentUp commentUp) {
        return commentUpMapper.insert(commentUp)>0;
    }

    /**
     * 删除
     *
     * @param userId,commentId
     */
    @Override
    public boolean delete(Integer userId, Integer commentId) {
        return commentUpMapper.delete(userId, commentId)>0;
    }

    /**
     * 根据主键查询整个对象
     *
     * @param id
     */
    @Override
    public CommentUp selectByPrimaryKey(Integer id) {
        return commentUpMapper.selectByPrimaryKey(id);
    }
}
