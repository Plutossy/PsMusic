package com.example.psmusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.psmusic.domain.SongList;
import com.example.psmusic.service.SongListService;
import com.example.psmusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

// 歌单
@RestController
@RequestMapping("/songList")
public class SongListController {
    @Autowired
    private SongListService songListService;

    //    添加歌单
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSongList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String title = request.getParameter("title").trim(); // 标题
        String pic = request.getParameter("pic").trim(); // 歌单图片
        String introduction = request.getParameter("introduction").trim(); // 简介
        String style = request.getParameter("style").trim(); // 风格
        // 保存到歌单对象中
        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        // 保存到数据库
        boolean flag = songListService.insert(songList);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "添加成功");
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "添加失败");
        }
        return jsonObject;
    }

    //    修改歌单
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSongList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim(); // 主键 id
        String title = request.getParameter("title").trim(); // 标题
        String introduction = request.getParameter("introduction").trim(); // 简介
        String style = request.getParameter("style").trim(); // 风格
        // 更新到歌手对象中
        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        // 更新到数据库
        boolean flag = songListService.update(songList);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改成功");
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "修改失败");
        }
        return jsonObject;
    }

    //    删除歌单
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSongList(HttpServletRequest request) {
        // 先查询到头像图片存放地址删除文件
        String id = request.getParameter("id").trim(); // 主键 id
        SongList songList = songListService.selectByPrimaryKey(Integer.parseInt(id));
        String[] strings = songList.getPic().split("/");
        String picName = strings[strings.length - 1];
        String pathName = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "songListPic" + System.getProperty("file.separator")
                + picName;
        File file = new File(pathName);
        file.delete();
        // 删除数据库中的数据
        boolean flag = songListService.delete(Integer.parseInt(id));
        return flag;
    }

    //    根据主键 id 查询歌单
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim(); // 主键 id
        return songListService.selectByPrimaryKey(Integer.parseInt(id));
    }

    //    查询所有歌单
    @RequestMapping(value = "/allSongList", method = RequestMethod.GET)
    public Object allSongList(HttpServletRequest request) {
        // System.out.println(songListService.allSongList());
        return songListService.allSongList();
    }

    //    根据歌单标题精确查询
    @RequestMapping(value = "/songListOfTitle", method = RequestMethod.GET)
    public Object songListOfTitle(HttpServletRequest request) {
        String title = request.getParameter("title").trim(); // 标题
        return songListService.songListOfTitle(title);
    }

    //    根据歌单标题模糊查询
    @RequestMapping(value = "/likeTitle", method = RequestMethod.GET)
    public Object likeTitle(HttpServletRequest request) {
        String title = request.getParameter("title").trim(); // 标题
        return songListService.likeTitle("%" + title + "%");
    }

    //    根据歌单风格模糊查询
    @RequestMapping(value = "/likeStyle", method = RequestMethod.GET)
    public Object likeStyle(HttpServletRequest request) {
        String style = request.getParameter("style").trim(); // 风格
        return songListService.likeStyle("%" + style + "%");
    }

    // 更新歌单头像
    @RequestMapping(value = "/updateSongListPic", method = RequestMethod.POST)
    public Object updateSongListPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        //  判断文件是否为空
        if (avatarFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败");
            return jsonObject;
        }
        //  文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + avatarFile.getOriginalFilename();
        //  文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "songListPic";
        //  如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        // 存储到数据库里的相对文件地址
        String storeAvatarPath = "/img/songListPic/" + fileName;
        try {
            // 保存文件
            avatarFile.transferTo(dest); // 上传文件
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(storeAvatarPath);
            boolean flag = songListService.update(songList);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "上传成功");
                jsonObject.put("pic", storeAvatarPath);
                return jsonObject;
            } else {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "上传失败");
                return jsonObject;
            }
        } catch (IOException e) {
            // throw new RuntimeException(e);
            // e.printStackTrace();
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "上传失败"+e.getMessage());
        } finally {
            return jsonObject; // 错误上传完成,返回空对象
        }
    }
}
