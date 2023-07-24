package com.example.psmusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.psmusic.domain.CommentUp;
import com.example.psmusic.service.CommentUpService;
import com.example.psmusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论控制类
 */
@RestController
@RequestMapping("/commentUp")
public class CommentUpController {

    @Autowired
    private CommentUpService commentUpService;

    /**
     * 添加评论
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addCommentUp(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");           //用户id
        String commentId = request.getParameter("commentId");           //歌曲id

        //保存到评论的对象中
        CommentUp commentUp = new CommentUp();
        commentUp.setUserId(Integer.parseInt(userId));
        commentUp.setCommentId(Integer.parseInt(commentId));
        boolean flag = commentUpService.insert(commentUp);
        if (flag) {   //保存成功
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "点赞成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "点赞失败");
        return jsonObject;
    }

    /**
     * 删除点赞
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteCommentUp(HttpServletRequest request) {
        String userId = request.getParameter("userId").trim();          //用户id
        String commentId = request.getParameter("commentId").trim();          //评论id
        boolean flag = commentUpService.delete(Integer.parseInt(userId), Integer.parseInt(commentId));
        return flag;
    }

    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim();          //主键
        return commentUpService.selectByPrimaryKey(Integer.parseInt(id));
    }

}






















