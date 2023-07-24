package com.example.psmusic.domain;

import java.io.Serializable;

// 歌单
public class SongList implements Serializable {
    // 主键
    private Integer id;
    // 歌单标题
    private String title;
    // 歌单图片
    private String pic;
    // 歌单简介
    private String introduction;
    // 歌单风格
    private String style;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
