<template>
  <div class="play-bar" :class="{show: !toggle}">
    <div @click="toggle=!toggle" class="item-up" :class="{turn: toggle}">
      <svg class="icon">
        <use xlink:href="#icon-jiantou-xia-cuxiantiao"></use>
      </svg>
    </div>
    <div class="kongjian">
      <!-- 上一首 -->
      <div class="item" @click="prev" onselectstart="return false">
        <svg class="icon">
          <use xlink:href="#icon-ziyuanldpi"></use>
        </svg>
      </div>
      <!-- 播放 -->
      <div class="item" @click="togglePlay" onselectstart="return false">
        <svg class="icon">
          <use :xlink:href="playButtonUrl"></use>
        </svg>
      </div>
      <!-- 下一首 -->
      <div class="item" @click="next">
        <svg class="icon">
          <use xlink:href="#icon-ziyuanldpi1"></use>
        </svg>
      </div>
      <!-- 歌曲图片 -->
      <div class="item-img" onselectstart="return false" @click="toLyric">
        <img :src="picUrl" />
        <i class="pic-top el-icon-top"></i>
      </div>
      <!-- 播放进度 -->
      <div class="playing-speed">
        <!-- 播放开始时间 -->
        <div class="current-time">{{ nowTime }}</div>
        <div class="progress-box">
          <div class="item-song-title">
            <div>{{ this.title }}</div>
            <div>{{ this.artist }}</div>
          </div>
          <div ref="progress" class="progress" @mousemove="mousemove">
            <!-- 进度条 -->
            <div ref="bg" class="bg" @click="updatemove">
              <div ref="curProgress" class="cur-progress" :style="{width: curLength + '%'}"></div>
            </div>
            <!-- 拖动的点 -->
            <!-- onselectstart禁止复制，优化体验 -->
            <div ref="idot" class="idot" :style="{left: curLength + '%'}" @mousedown="mousedown" @mouseup="mouseup" onselectstart="return false"></div>
          </div>
        </div>
        <!-- 播放结束时间 -->
        <div class="left-time">{{ songTime }}</div>
        <!-- 音量 -->
        <div class="item item-volume">
          <svg v-if="volume == 0" class="icon">
              <use xlink:href="#icon-yinliangjingyinheix"></use>
          </svg>
          <svg v-else class="icon">
            <use xlink:href="#icon-yinliang1"></use>
          </svg>
          <el-slider class="volume" v-model="volume" :vertical="true"></el-slider>
        </div>
        <!-- 收藏 -->
        <div class="item" @click="collection">
          <svg :class="{active: isActive}" class="icon">
            <use xlink:href="#icon-xihuan-shi"></use>
          </svg>
        </div>
        <!-- 下载 -->
        <div class="item" @click="download">
          <svg class="icon">
            <use xlink:href="#icon-xiazai"></use>
          </svg>
        </div>
        <!-- 当前播放歌曲列表 -->
        <div class="item hideAside-w" @click="changeAside">
          <svg class="icon hideAside-w">
            <use xlink:href="#icon-liebiao" class="hideAside-w"></use>
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
// import { mixin } from '@/mixins'
import { download, setCollect, getCollectOfUserId } from '@/api/index.js'

export default {
  // mixins: [mixin],
  name: 'play-bar',
  data () {
    return {
      nowTime: '00:00', // 当前播放时间
      songTime: '00:00', // 歌曲总时间
      curLength: 0, // 当前播放长度
      progressLength: 0, // 歌曲总长度
      mouseStartX: 0, // 拖拽开始位置
      tag: false, // 是否拖拽
      volume: 50, // 音量,默认一半
      toggle: true // 显示隐藏播放器页面
    }
  },
  computed: {
    ...mapGetters([
      'id', // 歌曲id
      'url', // 歌曲地址
      'isPlay', // 播放状态
      'playButtonUrl', // 播放按钮
      'picUrl', // 正在播放的歌曲图片
      'title', // 歌曲名
      'artist', // 歌手名
      'duration', // 歌曲时长
      'curTime', // 当前播放时间
      'showAside', // 是否显示播放中的歌曲列表
      'listIndex', // 当前歌曲在歌单中的位置
      'listOfSongs', // 当前歌单列表
      'autoNext', // 自动播放下一首
      'isLogin', // 用户是否已登录
      'userId', // 当前登录用户的id
      'isActive' // 当前播放的歌曲是否已收藏
    ])
  },
  watch: {
    // 监听播放还是暂停
    isPlay () {
      if (this.isPlay) {
        this.$store.commit('setPlayButtonUrl', '#icon-zanting')
      } else {
        this.$store.commit('setPlayButtonUrl', '#icon-bofang')
      }
    },
    curTime () {
      this.nowTime = this.formatSeconds(this.curTime)
      this.songTime = this.formatSeconds(this.duration)
      this.curLength = this.curTime / this.duration * 100
    },
    // 监听音量
    volume () {
      this.$store.commit('setVolume', this.volume / 100)
    },
    // 自动播放下一首
    autoNext () {
      this.next()
    }
  },
  mounted () {
    this.progressLength = this.$refs.progress.getBoundingClientRect().width
    document.querySelector('.item-volume').addEventListener('click', (e) => {
      document.querySelector('.volume').classList.toggle('show-volume')
      e.stopPropagation()
    }, false)
    document.querySelector('.volume').addEventListener('click', (e) => {
      e.stopPropagation()
    }, false)
    document.addEventListener('click', () => {
      document.querySelector('.volume').classList.remove('show-volume')
    }, false)
  },
  methods: {
    // 控制音乐播放、暂停
    togglePlay () {
      // 通过$nextTick()方法，确保拿到最新的DOM节点
      this.$nextTick(() => {
        if (this.isPlay) {
          this.$store.commit('setIsPlay', false)
        } else {
          this.$store.commit('setIsPlay', true)
        }
        this.$store.commit('setIsActive', false)
        if (this.isLogin) {
          getCollectOfUserId(this.userId).then(res => {
            res.some(item => {
              if (item.songId === this.listOfSongs[this.listIndex].id) {
                this.$store.commit('setIsActive', true)
                return true
              }
            })
          })
        }
      })
    },
    // 格式化时间
    formatSeconds (val) {
      let result = ''
      let theTime = parseFloat(val)
      let hour = parseInt(theTime / 3600) // 小时
      let minute = parseInt((theTime / 60) % 60) // 分钟
      let second = parseInt(theTime % 60) // 秒
      if (hour > 0) {
        if (hour < 10) {
          result = '0' + hour + ':'
        } else {
          result = hour + ':'
        }
      }
      if (minute > 0) {
        if (minute < 10) {
          result += '0' + minute + ':'
        } else {
          result += minute + ':'
        }
      } else {
        result += '00:'
      }
      if (second) {
        if (second < 10) {
          result += '0' + second
        } else {
          result += second
        }
      } else {
        result += '00'
      }
      return result
    },
    mousedown (e) {
      this.mouseStartX = e.clientX
      this.tag = true
      // 解决鼠标移除当前窗口抬起后还能拖动的bug
      window.addEventListener('mouseout', () => {
        this.tag = false
      })
      // 解决鼠标在当前窗口别的地方抬起后还能拖动的bug
      window.addEventListener('mouseup', () => {
        this.tag = false
      })
    },
    mouseup () {
      this.tag = false
    },
    mousemove (e) {
      if (!this.duration) return false
      if (this.tag) {
        let moveX = e.clientX - this.mouseStartX // 点移动的距离
        let curLength = this.$refs.curProgress.getBoundingClientRect().width // 当前进度条长度
        let newPercent = ((curLength + moveX) / this.progressLength) * 100 // 新的进度条百分比乘以100
        if (newPercent > 100) newPercent = 100
        this.curLength = newPercent
        this.mouseStartX = e.clientX
        this.changeTime(newPercent)
      }
    },
    // 更改歌曲进度
    changeTime (percent) {
      let newCurTime = percent * this.duration / 100
      this.$store.commit('setChangeTime', newCurTime)
    },
    // 点击播放条切换播放进度
    updatemove (e) {
      if (!this.tag) {
        let curLength = this.$refs.bg.offsetLeft // 当前进度条左侧坐标
        let newPercent = ((e.clientX - curLength) / this.progressLength) * 100 // 新的进度条百分比乘以100
        if (newPercent < 0) newPercent = 0
        if (newPercent > 100) newPercent = 100
        this.curLength = newPercent
        this.changeTime(newPercent)
      }
    },
    // 显示隐藏播放列表
    changeAside () {
      this.$store.commit('setShowAside', !this.showAside)
    },
    // 上一首
    prev () {
      if (this.listIndex !== -1 && this.listOfSongs.length > 1) { // 当前处于不可能状态，或者只有一首音乐的时候，不执行
        if (this.listIndex > 0) { // 如果当前歌曲不是第一首
          this.$store.commit('setListIndex', this.listIndex - 1) // 切换到上一首
        } else { // 如果当前歌曲是第一首,则切换到最后一首
          this.$store.commit('setListIndex', this.listOfSongs.length - 1)
        }
        this.toPlay(this.listOfSongs[this.listIndex].url)
      }
    },
    // 下一首
    next () {
      if (this.listIndex !== -1 && this.listOfSongs.length > 1) { // 当前处于不可能状态，或者只有一首音乐的时候，不执行
        if (this.listIndex < this.listOfSongs.length - 1) { // 如果当前歌曲不是最后一首
          this.$store.commit('setListIndex', this.listIndex + 1) // 切换到下一首
        } else { // 如果当前歌曲是最后一首,则切换到第一首
          this.$store.commit('setListIndex', 0)
        }
        this.toPlay(this.listOfSongs[this.listIndex].url)
      }
    },
    // 播放歌曲
    toPlay: function (url) {
      if (url && url !== this.url) {
        this.$store.commit('setId', this.listOfSongs[this.listIndex].id)
        this.$store.commit('setUrl', this.$store.state.config.HOST + url)
        this.$store.commit('setPicUrl', this.$store.state.config.HOST + this.listOfSongs[this.listIndex].pic)
        this.$store.commit('setTitle', this.replaceSong(this.listOfSongs[this.listIndex].name))
        this.$store.commit('setArtist', this.replaceName(this.listOfSongs[this.listIndex].name))
        this.$store.commit('setLyric', this.parseLyric(this.listOfSongs[this.listIndex].lyric))
        this.$store.commit('setIsActive', false)
        if (this.isLogin) {
          getCollectOfUserId(this.userId).then(res => {
            res.some(item => {
              if (item.songId === this.listOfSongs[this.listIndex].id) {
                this.$store.commit('setIsActive', true)
                return true
              }
            })
          })
        }
      }
    },
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
    },
    // 跳转到歌词页面
    toLyric () {
      this.$router.push(`/lyric`, () => {
        this.$store.commit('setLyric', this.parseLyric(this.listOfSongs[this.listIndex].lyric))
      }, () => {})
    },
    // 下载歌曲
    download () {
      download(this.url).then(res => {
        let content = res.data
        let eleLink = document.createElement('a')
        eleLink.download = `${this.artist}-${this.title}.mp3`
        eleLink.style.display = 'none'
        // 把字符内容转换为blob地址
        let blob = new Blob([content])
        eleLink.href = URL.createObjectURL(blob)
        // 把链接地址加到document中
        document.body.appendChild(eleLink)
        // 触发点击
        eleLink.click()
        // 然后移除
        document.body.removeChild(eleLink)
      }).catch(err => {
        console.log(err)
        this.$message({
          message: '下载失败',
          type: 'error'
        })
      })
    },
    // 收藏歌曲
    collection () {
      if (this.isLogin) {
        let params = new URLSearchParams()
        params.append('userId', this.userId)
        params.append('type', 0)
        params.append('songId', this.id)
        setCollect(params).then(res => {
          if (res.code === 1) {
            this.$store.commit('setIsActive', true)
            this.$notify({
              title: '收藏成功',
              type: 'success'
            })
          } else if (res.code === 2) {
            this.$notify({
              title: '已收藏过该歌曲',
              type: 'warning'
            })
          } else {
            this.$message.error('收藏失败')
          }
        })
      } else {
        this.$notify({
          title: '要不, 咱先登录再试试',
          type: 'warning'
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/play-bar.scss';
</style>
