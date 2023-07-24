package com.example.psmusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.psmusic.domain.Singer;
import com.example.psmusic.service.SingerService;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    private SingerService singerService;

    //    添加歌手
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSinger(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name").trim(); // 姓名
        String sex = request.getParameter("sex").trim(); // 性别
        String pic = request.getParameter("pic").trim(); // 头像
        String birth = request.getParameter("birth").trim(); // 生日
        String location = request.getParameter("location").trim(); // 地区
        String introduction = request.getParameter("introduction").trim(); // 简介
        // 生日日期格式转换
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            //  throw new RuntimeException(e);
            e.printStackTrace();
        }
        // 保存到歌手对象中
        Singer singer = new Singer();
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setPic(pic);
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        // 保存到数据库
        boolean flag = singerService.insert(singer);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "添加成功");
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "添加失败");
        }
        return jsonObject;
    }

    //    修改歌手
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSinger(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim(); // 主键 id
        String name = request.getParameter("name").trim(); // 姓名
        String sex = request.getParameter("sex").trim(); // 性别
        String birth = request.getParameter("birth").trim(); // 生日
        String location = request.getParameter("location").trim(); // 地区
        String introduction = request.getParameter("introduction").trim(); // 简介
        // 生日日期格式转换
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            //  throw new RuntimeException(e);
            e.printStackTrace();
        }
        // 更新到歌手对象中
        Singer singer = new Singer();
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        // 更新到数据库
        boolean flag = singerService.update(singer);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改成功");
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "修改失败");
        }
        return jsonObject;
    }

    //    删除歌手
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSinger(HttpServletRequest request) {
        // 先查询到头像图片存放地址删除文件
        String id = request.getParameter("id").trim(); // 主键 id
        Singer singer = singerService.selectByPrimaryKey(Integer.parseInt(id));
        String[] strings = singer.getPic().split("/");
        String picName = strings[strings.length - 1];
        String pathName = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "singerPic" + System.getProperty("file.separator")
                + picName;
        File file = new File(pathName);
        file.delete();
        // 删除数据库中的数据
        boolean flag = singerService.delete(Integer.parseInt(id));
        return flag;
    }

    //    根据主键 id 查询歌手
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim(); // 主键 id
        return singerService.selectByPrimaryKey(Integer.parseInt(id));
    }

    //    查询所有歌手
    @RequestMapping(value = "/allSinger", method = RequestMethod.GET)
    public Object allSinger(HttpServletRequest request) {
        // System.out.println(singerService.allSinger());
        return singerService.allSinger();
    }

    //    根据歌手名字模糊查询
    @RequestMapping(value = "/singerOfName", method = RequestMethod.GET)
    public Object singerOfName(HttpServletRequest request) {
        String name = request.getParameter("name").trim(); // 歌手名字
        return singerService.singerOfName("%" + name + "%");
    }

    //    根据性别查询
    @RequestMapping(value = "/singerOfSex", method = RequestMethod.GET)
    public Object singerOfSex(HttpServletRequest request) {
        String sex = request.getParameter("sex").trim(); // 性别
        return singerService.singerOfSex(Integer.parseInt(sex));
    }

    // 更新歌手头像
    @RequestMapping(value = "/updateSingerPic", method = RequestMethod.POST)
    public Object updateSingerPic(@RequestParam("file") MultipartFile avatarFile, @RequestParam("id") int id) {
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
                + System.getProperty("file.separator") + "singerPic";
        //  如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        // 实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        // 存储到数据库里的相对文件地址
        String storeAvatarPath = "/img/singerPic/" + fileName;
        try {
            // 保存文件
            avatarFile.transferTo(dest); // 上传文件
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatarPath);
            boolean flag = singerService.update(singer);
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
