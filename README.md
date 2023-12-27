# 基于 SpringBoot + Mybatis + Vue2 的音乐网站设计与实现

## 一、介绍

本音乐网站叫做 `PsMusic`
`A music website system based on SpringBoot+Vue2, which can realize all operations related to music`

## 二、使用技术
> Vue2 + SpringBoot2.x + mysql8.x
> vuex + vue-router + mybatis + scss

## 三、使用软件
> vscode、navicat、idea

## 四、设计实现
前后端分离的一个基于 SpringBoot + Vue2 的音乐网站系统，能够实现有关音乐的所有操作，比如：歌手管理、音乐管理、评论收藏等等；

主要分为**前台、后台**两个部分；
**前台**主要包括：登录注册功能、歌单管理、歌手管理、我的音乐、关键词搜索和设置退出功能等等，最重要的是前台还包括自定义播放条、评论、收藏以及听歌的功能等等多方面。
**后台**主要包括：管理员登录退出功能、后台首页统计功能、用户管理、歌手管理、歌单管理、歌曲管理、收藏管理等等

## 六、数据库设计
主要是建立 管理员信息表、用户信息表、歌手信息表、歌单信息表、收藏信息表、点赞信息表、评论信息表、歌曲信息表等等，如下图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/9acbc95bc8664062a43014d03fdfe67b.png#pic_center)
数据库文件在github中保存，PsMusicSQL.sql 文件，需要自取。

## 七、项目截图
### 7.1 前台部分（剔除相似部分）：
**前台首页**![前台首页1](https://img-blog.csdnimg.cn/a0a9aba0ff5544859fab8a5887b46bbc.png#pic_center)
![前台首页2](https://img-blog.csdnimg.cn/1e5e15eac00a40108d3da571b5e843de.png#pic_center)
**前台歌单**
![前台歌单](https://img-blog.csdnimg.cn/907226a3494c48d49ae566c6c57508e3.png#pic_center)
**前台我的音乐**
![我的音乐](https://img-blog.csdnimg.cn/789148e4366f43c5a21e92241af8f4c6.png#pic_center)
**前台搜索**
![搜索](https://img-blog.csdnimg.cn/50861c5523864e11bd0712ce6db054cc.png#pic_center)
**前台用户注册**
![用户注册](https://img-blog.csdnimg.cn/a00d89c59af547a6b5bd09e323e4b49e.png#pic_center)

### 7.2 后台部分（剔除相似部分）：
**后台首页（统计）**
![后台首页统计部分](https://img-blog.csdnimg.cn/b31e0cdb954e4f3083dcda0ca4d2ad79.png#pic_center)
**歌手管理**
![歌手管理](https://img-blog.csdnimg.cn/2c4c8ae403f9457ba82b4599fe0a7bda.png#pic_center)
**歌曲管理**
![歌曲管理](https://img-blog.csdnimg.cn/8726e1b74c884ea38281d6d930e28999.png#pic_center)
## 八、运行项目
<font color="red">注意在manage和client两个文件中都要进行安装运行</font>
### 8.1 安装
自行导入数据库
```js
npm i
```

### 8.2 运行
```js
npm run dev
```

###  8.3 打包发布

```js
npm run build
```
## 九、技术亮点
1. 后台主要使用 vue 组件完成开发、二次封装 Axios，vue-router 来传递和获取数据，同时设置导航守卫来过滤 token 权限的路径。
2. 前台主要使用 vuex 来传递和共享数据，使用动态组件和插槽修改完善内容，也多次使用编程式导航来实现路径的跳转，结合 Animate.css 插件来优化页面。

## 十、注意事项

> 1、可能版本有所老化，所有可以下载 node_modules 文件夹一并导入项目，就不必运行 npm i 了

> 2、本系统只用于记录个人学习情况，如有它用请联系本人 (标明备注)

> 3、联系方式：
> CSDN：https://blog.csdn.net/Pluto_ssy
> 微信: zhlysun

## <font color="skyblue">十一、Github地址</font>
[点击这里，这是我的 PsMusic 代码地址](https://github.com/Plutossy/PsMusic)
<font color="skyblue">劳烦github star 一下吧！这对我真的很重要</font>