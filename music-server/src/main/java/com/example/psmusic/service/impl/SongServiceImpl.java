package com.example.psmusic.service.impl;

import com.example.psmusic.dao.SongMapper;
import com.example.psmusic.domain.Song;
import com.example.psmusic.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 歌曲Service实现类
@Service
public class SongServiceImpl implements SongService {
    @Autowired
    private SongMapper songMapper;

    /**
     * @param song
     * @return
     */
    @Override
    public boolean insert(Song song) {
        return songMapper.insert(song) > 0;
    }

    /**
     * @param song
     * @return
     */
    @Override
    public boolean update(Song song) {
        return songMapper.update(song) > 0;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        return songMapper.delete(id) > 0;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Song selectByPrimaryKey(Integer id) {
        return songMapper.selectByPrimaryKey(id);
    }

    /**
     * @return
     */
    @Override
    public List<Song> allSong() {
        return songMapper.allSong();
    }

    /**
     * @param name
     * @return
     */
    @Override
    public List<Song> songOfName(String name) {
        return songMapper.songOfName(name);
    }

    /**
     * @param name
     * @return
     */
    @Override
    public List<Song> likeSongOfName(String name) {
        return songMapper.likeSongOfName('%' + name + '%');
    }

    /**
     * @param singerId
     * @return
     */
    @Override
    public List<Song> songOfSingerId(Integer singerId) {
        return songMapper.songOfSingerId(singerId);
    }
}
