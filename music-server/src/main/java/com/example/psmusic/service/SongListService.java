package com.example.psmusic.service;

import com.example.psmusic.domain.SongList;

import java.util.List;

// 歌单service接口
public interface SongListService {
    //    增加
    public boolean insert(SongList songList);

    //    修改
    public boolean update(SongList songList);

    //    删除
    public boolean delete(Integer id);

    //    根据主键查询对象
    public SongList selectByPrimaryKey(Integer id);

    //    查询所有歌单
    public List<SongList> allSongList();

    //    根据歌单标题精确查询
    public List<SongList> songListOfTitle(String title);

    //    根据歌单标题模糊查询
    public List<SongList> likeTitle(String title);

    //   根据歌单风格模糊查询
    public List<SongList> likeStyle(String style);
}
