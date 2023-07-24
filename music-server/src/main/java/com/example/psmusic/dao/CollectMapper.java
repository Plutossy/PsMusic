package com.example.psmusic.dao;

import com.example.psmusic.domain.Collect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收藏Dao
 */
@Repository
public interface CollectMapper {
    /**
     *增加
     */
    public int insert(Collect collect);

    /**
     * 删除
     */
    public int delete(Integer id);

    /**
     * 根据用户id和歌曲id删除
     */
    public int deleteByUserIdSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);

    /**
     * 查询所有收藏
     */
    public List<Collect> allCollect();

    /**
     * 查询某个用户的收藏列表
     */
    public List<Collect> collectOfUserId(Integer userId);

    /**
     * 查询某个用户是否已经收藏了某个歌曲
     */
    public int existSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);

}
















