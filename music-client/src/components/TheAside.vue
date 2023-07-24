<template>
  <transition name="slide-fade">
    <div class="the-aside" v-if="showAside">
      <h2 class="title">播放列表</h2>
      <ul class="menus">
        <li v-for="(item, index) in listOfSongs" :key="index" :class="{'is-play': id == item.id}" @click="toPlay(item.id, item.url, item.pic, item.index, item.name, item.lyric)">
          <p class="songName">{{ replaceSong(item.name) }}</p>
          <p class="sinerName">{{ replaceName(item.name) }}</p>
        </li>
      </ul>
    </div>
  </transition>
</template>

<script>
import { mapGetters } from 'vuex'
import { getCollectOfUserId } from '@/api/index.js'

export default {
  name: 'the-aside',
  computed: {
    ...mapGetters([
      'showAside', // 是否显示播放中的歌曲列表
      'listOfSongs', // 当前歌曲列表
      'id', // 正在播放歌曲id
      'isLogin', // 用户是否已登录
      'userId', // 当前登录用户的id
      'isActive' // 当前播放的歌曲是否已收藏
    ])
  },
  mounted () {
    // 点击其他地方隐藏播放列表
    document.addEventListener('click', (e) => {
      // console.log(e.target.classList)
      // e.target.classList 表示点击的元素的所有类名
      if (!e.target.classList.contains('hideAside-w')) {
        this.$store.commit('setShowAside', false)
      }
    })
  },
  methods: {
    // 获取歌手名-歌名前半部分 ---歌手名
    replaceName (str) {
      let arr = str.split('-')
      return arr[0]
    },
    // 获取歌手名-歌名后半半部分 ---歌名
    replaceSong (str) {
      let arr = str.split('-')
      return arr[1]
    },
    // 播放歌曲
    toPlay: function (id, url, pic, index, name, lyric) {
      this.$store.commit('setId', id)
      this.$store.commit('setUrl', this.$store.state.config.HOST + url)
      this.$store.commit('setPicUrl', this.$store.state.config.HOST + pic)
      this.$store.commit('setListIndex', index)
      this.$store.commit('setTitle', this.replaceSong(name))
      this.$store.commit('setArtist', this.replaceName(name))
      this.$store.commit('setLyric', this.parseLyric(lyric))
      this.$store.commit('setIsActive', false)
      if (this.isLogin) {
        getCollectOfUserId(this.userId).then(res => {
          res.some(item => {
            if (item.songId === id) {
              this.$store.commit('setIsActive', true)
              return true
            }
          })
        })
      }
    },
    // 解析歌词
    parseLyric (text) {
      let lines = text.split('\n') // 将文本按行分割，存入数组
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g // [00:00.00]或者[00:00.000]的正则表达式
      let result = [] // 返回值
      // 对于歌词格式不对的直接返回
      if (!(/\[.+\]/.test(text))) {
        return [[0, text]]
      }
      while (!pattern.test(lines[0])) {
        lines = lines.slice(1)
      }
      // 遍历每一行，提取歌词和对应的时间
      for (let item of lines) {
        let time = item.match(pattern) // 存前面的时间段
        let value = item.replace(pattern, '') // 存后面的歌词
        for (let item1 of time) {
          let t = item1.slice(1, -1).split(':') // 分钟和秒
          if (value !== '') {
            result.push([parseInt(t[0], 10) * 60 + parseFloat(t[1]), value]) // 转化为秒
          }
        }
      }
      // 根据第一个元素大小排序
      result.sort(function (a, b) {
        return a[0] - b[0]
      })
      return result
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-aside.scss';
.songName {
  font-size: 14px;
  color: #333;
  margin-bottom: 5px;
}
.sinerName {
  font-size: 8px;
  color: #999;
}
</style>
