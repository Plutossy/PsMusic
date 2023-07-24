package com.example.psmusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.psmusic.domain.ListSong;
import com.example.psmusic.service.ListSongService;
import com.example.psmusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 歌单的歌曲管理controller
 */
@RestController
@RequestMapping("/listSong")
public class ListSongController {

    @Autowired
    private ListSongService listSongService;

    /**
     * 给歌单添加歌曲
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addListSong(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        //获取前端传来的参数
        String songId = request.getParameter("songId").trim();  //歌曲id
        String songListId = request.getParameter("songListId").trim(); //歌单id
        // 将参数封装到ListSong对象中
        ListSong listSong = new ListSong();
        listSong.setSongId(Integer.parseInt(songId));
        listSong.setSongListId(Integer.parseInt(songListId));
        boolean flag = listSongService.insert(listSong);
        // 将操作结果返回给前端
        if(flag){
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"保存成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"保存失败");
        return jsonObject;

    }

    /**
     * 根据歌单id查询歌曲
     */
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public Object detail(HttpServletRequest request){
        String songListId = request.getParameter("songListId");
        return listSongService.listSongOfSongListId(Integer.parseInt(songListId));
    }

    // 修改歌曲信息
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public Object updateListSong(HttpServletRequest request) {
//        JSONObject jsonObject = new JSONObject();
//        String id = request.getParameter("id").trim(); // 歌单id
//        String songId = request.getParameter("songId").trim();  //歌曲id
//        String songListId = request.getParameter("songListId").trim(); //歌单id
        // 保存到歌手对象数据库
//        ListSong listSong = new ListSong();
//        listSong.setId(Integer.parseInt(id));
//        listSong.setSongId(Integer.parseInt(songId));
//        listSong.setSongListId(Integer.parseInt(songListId));
//        boolean flag = listSongService.update(listSong);
//        if (flag) { // 成功
//            jsonObject.put(Consts.CODE, 1);
//            jsonObject.put(Consts.MSG, "修改成功");
//            return jsonObject;
//        } else {
//            jsonObject.put(Consts.CODE, 0);
//            jsonObject.put(Consts.MSG, "修改失败");
//            return jsonObject;
//        }
//    }

    /**
     * 删除歌单里的歌曲
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object delete(HttpServletRequest request){
        String songId = request.getParameter("songId").trim();                 //歌曲id
        String songListId = request.getParameter("songListId").trim();        //歌单id
        boolean flag = listSongService.deleteBySongIdAndSongListId(Integer.parseInt(songId),Integer.parseInt(songListId));
        return flag;
    }

}




















