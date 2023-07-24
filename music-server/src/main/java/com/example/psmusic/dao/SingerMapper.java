package com.example.psmusic.dao;

import com.example.psmusic.domain.Singer;
import org.springframework.stereotype.Repository;

import java.util.List;

// 歌手Dao
@Repository
public interface SingerMapper {
    //    增加
    public int insert(Singer singer);

    //    修改
    public int update(Singer singer);

    //    删除
    public int delete(Integer id);

    //    根据主键查询对象
    public Singer selectByPrimaryKey(Integer id);

    //    查询所有歌手
    public List<Singer> allSinger();

    //    根据歌手名字模糊查询
    public List<Singer> singerOfName(String name);

    //    根据性别查询
    public List<Singer> singerOfSex(Integer sex);
}
