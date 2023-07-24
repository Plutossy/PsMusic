<template>
  <div class="the-header animate__animated animate__fadeInDown">
    <div class="header-logo animate__animated animate__bounceInUp" @click="goHome">
      <svg class="icon">
        <use xlink:href="#icon-erji"></use>
      </svg>
      <span>PsMusic</span>
    </div>
    <ul class="navbar">
      <li class="LgSn" :class="{active: item.name === activeName}" v-for="item in navMsg" :key="item.path" @click="goPage(item.path, item.name)">{{ item.name }}</li>
      <li>
        <div class="header-search">
          <input type="text" placeholder="搜索音乐" @keyup.enter="goSearch()" @keyup.esc="keywords=''" v-model.trim="keywords">
          <div class="search-btn" @click="goSearch">
            <svg class="icon">
              <use xlink:href="#icon-sousuo"></use>
            </svg>
          </div>
        </div>
      </li>
      <li class="LgSn" v-show="!isLogin" :class="{active: item.name === activeName}" v-for="item in loginMsg" :key="item.path" @click="goPage(item.path, item.name)">{{ item.name }}</li>
    </ul>
    <div class="header-right" v-show="isLogin">
      <div id="user">
        <img :src="attachImageUrl(avatar)" alt="头像">
      </div>
      <ul class="menu">
        <li v-for="(item, index) in userMenu" :key="index" @click="goUserMenu(item.path)">{{ item.name }}</li>
      </ul>
    </div>
    <a href="http://127.0.0.1:8880" target="_blank" class="signBs animate__animated animate__heartBeat">
      <p><i class="el-icon-right"></i>管理后台</p>
    </a>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { navMsg, loginMsg, userMenu } from '@/assets/data/header'
import { likeSongOfName } from '@/api/index.js'

export default {
  name: 'the-header',
  data () {
    return {
      navMsg: [], // 导航栏信息
      loginMsg: [], // 登录信息
      keywords: '', // 搜索关键字
      userMenu: [] // 用户菜单
    }
  },
  computed: {
    ...mapGetters([
      'activeName',
      'isLogin',
      'avatar'
    ])
  },
  created () {
    this.navMsg = navMsg
    this.loginMsg = loginMsg
    this.userMenu = userMenu
  },
  mounted () {
    this.$store.commit('setActiveName', '首页')
    document.querySelector('#user').addEventListener('click', (e) => {
      document.querySelector('.menu').classList.toggle('show')
      e.stopPropagation() // 阻止冒泡
    }, false)
    document.querySelector('.menu').addEventListener('click', (e) => {
      e.stopPropagation() // 点击菜单内部时，阻止冒泡，这样点击内部不会触发外部的点击事件
    }, false)
    // 点击其他地方，菜单消失
    document.addEventListener('click', () => {
      document.querySelector('.menu').classList.remove('show')
    }, false)
  },
  methods: {
    // 拼接图片地址
    attachImageUrl (srcUrl) {
      return srcUrl ? this.$store.state.config.HOST + srcUrl : '../assets/img/user.jpg'
    },
    goHome () {
      this.$router.push('/')
      this.$store.commit('setActiveName', '首页')
    },
    goPage (path, name) {
      if (!this.isLogin && path === '/my-music') {
        this.$notify({
          title: '请先登录, 才能进入该页面!',
          type: 'warning'
        })
        return
      }
      this.$router.push(path)
      this.$store.commit('setActiveName', name)
    },
    goSearch () {
      if (this.keywords) {
        this.$router.push({path: '/search', query: {keywords: this.keywords}}, () => {
          // 解决在搜索界面不能执行两次相同路由的问题
          likeSongOfName(this.$route.query.keywords).then(res => {
            if (res.length) {
              this.$store.commit('setListOfSongs', res)
            } else {
              this.$store.commit('setListOfSongs', [])
              this.$notify({
                title: '本系统暂时没有该歌曲, 试试别的吧！',
                type: 'warning'
              })
            }
          }).catch(err => {
            console.log(err)
          })
        }, () => {
          this.$notify({
            title: '不能重复搜索哦!',
            type: 'warning'
          })
        })
        this.$store.commit('setActiveName', '搜索')
      } else {
        this.$message({
          message: '输入内容不能为空',
          type: 'error'
        })
      }
    },
    goUserMenu (path) {
      if (path === 0) {
        this.$store.commit('setActiveName', '退出登录')
        this.$store.commit('setIsLogin', false) // 退出登录
        this.$store.commit('setIsActive', false)
        document.querySelector('.menu').classList.remove('show')
        this.$router.go(0) // 刷新页面
      } else {
        this.$router.push({path: path}, () => {
          document.querySelector('.menu').classList.remove('show')
          this.$store.commit('setActiveName', '设置')
        }, () => {
          document.querySelector('.menu').classList.remove('show')
          this.$notify({
            title: '别再点了, 没有其他页面了!',
            type: 'warning'
          })
        })
      }
    }
  }
}
</script>

<style  lang="scss" scoped>
@import '../assets/css/the-header.scss';
.the-header {
  background: url(../assets/img/music_bg9.gif) center no-repeat;
  background-size: cover;
}
.signBs {
  width: 120px;
  height: 40px;
  margin: auto;
  cursor: pointer;
  text-decoration: none;
  background-color: rgb(128, 128, 128, 0.1);
  border: 2px solid rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.3);
  p {
    color: brown;
    font-size: 16px;
    font-weight: 600;
    text-align: center;
    line-height: 40px;
    .el-icon-right {
      font-weight: 1000;
      font-size: 20px;
      line-height: 40px;
    }
  }
}
.signBs:hover {
  background-color: $theme-color;
}
.LgSn {
  height: 40px;
  line-height: 40px;
  background-color: rgb(128, 128, 128, 0.1);
  border: 2px solid rgba(0, 0, 0, 0.3);
  border-radius: 10px;
  box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.3);
}
</style>
