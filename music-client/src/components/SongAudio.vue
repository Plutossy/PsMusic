<template>
  <div class="song-audio">
    <audio ref="player"
      :src="url"
      controls = "controls"
      preload = "true"
      @canplay="startPlay"
      @ended="ended"
      @timeupdate="timeupdate"
      muted
    ></audio>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getCollectOfUserId } from '@/api/index.js'

export default {
  name: 'song-audio',
  computed: {
    ...mapGetters([
      'id', // 歌曲id
      'url', // 歌曲地址
      'isPlay', // 播放状态
      'listOfSongs', // 当前歌曲列表
      'curTime', // 当前音乐的播放位置
      'changeTime', // 指定播放时刻
      'autoNext', // 用于自动触发播放下一首
      'volume', // 音量
      'isLogin', // 用户是否已登录
      'userId', // 当前登录用户的id
      'isActive' // 当前播放的歌曲是否已收藏
    ])
  },
  watch: {
    // 监听播放还是暂停
    isPlay () {
      // 解决刷新后图标不对应的情况
      window.addEventListener('beforeunload', (e) => {
        // // 不是所有浏览器都支持提示信息的修改
        // var confirmationMessage = '请先保存您编辑的内容,否则您修改的信息会丢失。'
        // e.returnValue = confirmationMessage
        // return confirmationMessage
        this.$store.commit('setIsPlay', false)
        // this.$refs.player.pause()
      })
      this.togglePlay()
    },
    // 跳转到指定播放时刻
    changeTime () {
      this.$refs.player.currentTime = this.changeTime
    },
    // 改变音量
    volume (val) {
      this.$refs.player.volume = val
    }
  },
  methods: {
    // 获取链接后准备播放
    startPlay () {
      let player = this.$refs.player
      // 设置播放时长
      this.$store.commit('setDuration', player.duration)
      // 开始播放
      player.play()
      this.$store.commit('setIsPlay', true)
      // 设置收藏状态
      this.$store.commit('setIsActive', false)
      if (this.isLogin) {
        getCollectOfUserId(this.userId).then(res => {
          res.some(item => {
            if (item.songId === this.id) {
              this.$store.commit('setIsActive', true)
              return true
            }
          })
        })
      }
    },
    // 播放完成之后触发
    ended () {
      this.$store.commit('setIsPlay', false)
      this.$store.commit('setCurTime', 0)
      this.$store.commit('setAutoNext', !this.autoNext)
    },
    // 开始、暂停
    togglePlay () {
      // 通过$nextTick()方法，确保拿到最新的DOM节点
      this.$nextTick(() => {
        if (this.isPlay) {
          this.$refs.player.play()
        } else {
          this.$refs.player.pause() // 暂停
        }
      })
    },
    // 播放进度
    timeupdate () {
      this.$store.commit('setCurTime', this.$refs.player.currentTime)
    }
  }
}
</script>

<style scoped>
.song-audio {
  display: none;
}
</style>
