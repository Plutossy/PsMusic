package com.example.psmusic.service;

import com.example.psmusic.domain.Singer;

import java.util.List;

// 歌手Service接口
public interface SingerService {
    //    增加
    public boolean insert(Singer singer);

    //    修改
    public boolean update(Singer singer);

    //    删除
    public boolean delete(Integer id);

    //    根据主键查询对象
    public Singer selectByPrimaryKey(Integer id);

    //    查询所有歌手
    public List<Singer> allSinger();

    //    根据歌手名字模糊查询
    public List<Singer> singerOfName(String name);

    //    根据性别查询
    public List<Singer> singerOfSex(Integer sex);
}
