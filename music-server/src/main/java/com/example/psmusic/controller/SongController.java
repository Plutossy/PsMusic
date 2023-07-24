package com.example.psmusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.psmusic.domain.Song;
import com.example.psmusic.service.SongService;
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

// 歌曲Controller
@RestController
@RequestMapping("/song")
public class SongController {
    @Autowired
    private SongService songService;

    //    添加歌曲
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSong(HttpServletRequest request, @RequestParam("file") MultipartFile mpFile) {
        JSONObject jsonObject = new JSONObject();
        String singerId = request.getParameter("singerId").trim(); // 歌手id
        String name = request.getParameter("name").trim(); // 歌名
        String introduction = request.getParameter("introduction").trim(); // 简介
        String pic = "/img/songPic/tubiao.jpg"; // 默认图片
        String lyric = request.getParameter("lyric").trim(); // 歌词
        // 上传歌曲文件
        if (mpFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌曲上传失败");
            return jsonObject;
        }
        //  文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + mpFile.getOriginalFilename();
        //  文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        //  如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        // 存储到数据库里的相对文件地址
        String storeUrlPath = "/song/" + fileName;
        try {
            // 保存文件
            mpFile.transferTo(dest); // 上传文件
            Song song = new Song();
            song.setSingerId(Integer.parseInt(singerId));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setPic(pic);
            song.setLyric(lyric);
            song.setUrl(storeUrlPath);
            boolean flag = songService.insert(song);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "保存成功");
                jsonObject.put("pic", storeUrlPath);
                return jsonObject;
            } else {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "保存失败");
                return jsonObject;
            }
        } catch (IOException e) {
            // throw new RuntimeException(e);
            // e.printStackTrace();
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "保存失败"+e.getMessage());
        } finally {
            return jsonObject; // 错误上传完成,返回空对象
        }
    }

    // 根据歌手id查询歌曲
    @RequestMapping(value = "/singer/detail", method = RequestMethod.GET)
    public Object songOfSingerId(HttpServletRequest request) {
        String singerId = request.getParameter("singerId");
        return songService.songOfSingerId(Integer.parseInt(singerId));
    }

    // 修改歌曲信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSong(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim(); // 歌曲id
        // String singerId = request.getParameter("singerId").trim(); // 歌手id
        String name = request.getParameter("name").trim(); // 歌名
        String introduction = request.getParameter("introduction").trim(); // 专辑
        String lyric = request.getParameter("lyric").trim(); // 歌词
        // 保存到歌手对象数据库
        Song song = new Song();
        song.setId(Integer.parseInt(id));
        // song.setSingerId(Integer.parseInt(singerId));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setLyric(lyric);
        boolean flag = songService.update(song);
        if (flag) { // 成功
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改成功");
            return jsonObject;
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "修改失败");
            return jsonObject;
        }
    }

    // 删除歌曲
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSong(HttpServletRequest request) {
        // 先查询到歌曲和歌曲图片存放地址删除文件
        String id = request.getParameter("id").trim(); // 歌曲id
        Song song = songService.selectByPrimaryKey(Integer.parseInt(id));
        // 删除歌曲文件
        String filePath = System.getProperty("user.dir") + song.getUrl();
        File file1 = new File(filePath);
        file1.delete();

        // 删除歌曲图片
        String[] strings = song.getPic().split("/");
        String picName = strings[strings.length - 1];
        String pathName = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "songPic" + System.getProperty("file.separator")
                + picName;
        File file2 = new File(pathName);
        file2.delete();

        // 删除数据库中歌曲信息
        boolean flag = songService.delete(Integer.parseInt(id));
        return flag;
    }

    // 更新歌曲图片
    @RequestMapping(value = "/updateSongPic", method = RequestMethod.POST)
    public Object updateSongPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌曲图片上传失败");
            return jsonObject;
        }
        //  文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        //  文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "songPic";
        //  如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        // 存储到数据库里的相对文件地址
        String storeAvatarPath = "/img/songPic/" + fileName;
        try {
            // 保存文件
            avatorFile.transferTo(dest); // 上传文件
            Song song = new Song();
            song.setId(id);
            song.setPic(storeAvatarPath);
            boolean flag = songService.update(song);
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

    // 更新歌曲url
    @RequestMapping(value = "/updateSongUrl", method = RequestMethod.POST)
    public Object updateSongUrl(@RequestParam("file") MultipartFile File, @RequestParam("id") int id) {
        // 删除原来的歌曲
        Song song1 = songService.selectByPrimaryKey(id);
        String filePath1 = System.getProperty("user.dir") + song1.getUrl();
        File file1 = new File(filePath1);
        file1.delete();
        // 保存新的歌曲
        JSONObject jsonObject = new JSONObject();
        if (File.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌曲上传失败");
            return jsonObject;
        }
        //  文件名 = 当前时间到毫秒 + 原来的文件名
        String fileName = System.currentTimeMillis() + File.getOriginalFilename();
        //  文件路径
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        //  如果文件路径不存在，新增该路径
        File file2 = new File(filePath);
        if (!file2.exists()) {
            file2.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        // 存储到数据库里的相对文件地址
        String storeUrlPath = "/song/" + fileName;
        try {
            // 保存文件
            File.transferTo(dest); // 上传文件
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeUrlPath);
            boolean flag = songService.update(song);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "上传成功");
                jsonObject.put("songUrlPath", storeUrlPath);
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
            jsonObject.put(Consts.MSG, "上传失败" + e.getMessage());
        } finally {
            return jsonObject; // 错误上传完成,返回空对象
        }
    }

    /**
     * 根据歌曲id查询歌曲对象
     */
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public Object detail(HttpServletRequest request){
        String songId = request.getParameter("songId");
        return songService.selectByPrimaryKey(Integer.parseInt(songId));
    }

    /**
     * 根据歌曲名字精确查询歌曲
     */
    @RequestMapping(value = "/songOfSongName",method = RequestMethod.GET)
    public Object songOfSongName(HttpServletRequest request){
        String songName = request.getParameter("songName");
        return songService.songOfName(songName);
    }

    /**
     * 根据歌曲名字模糊查询歌曲
     */
    @RequestMapping(value = "/likeSongOfName",method = RequestMethod.GET)
    public Object likeSongOfName(HttpServletRequest request){
        String songName = request.getParameter("songName");
        return songService.likeSongOfName(songName);
    }

    // 查询所有歌曲
    @RequestMapping(value = "/allSong", method = RequestMethod.GET)
    public Object allSong(HttpServletRequest request) {
        return songService.allSong();
    }
}
