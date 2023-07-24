package com.example.psmusic.domain;

import java.io.Serializable;
import java.util.Date;

/*
 * 歌手
 */
public class Singer implements Serializable {
    //    主键
    private Integer id;
    //    账号
    private String name;
    //    性别
    private Byte sex;
    //    头像
    private String pic;
    //    生日
    private Date birth;
    //    地区
    private String location;
    //    简介
    private String introduction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

}