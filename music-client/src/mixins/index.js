import { mapGetters } from 'vuex'
import { likeSongOfName, getCollectOfUserId } from '@/api/index.js'

export const mixin = {
  computed: {
    ...mapGetters([
      'loginIn', // 用户是否已登录
      'userId', // 当前登录用户的id
      'id' // 当前播放歌曲的id
    ])
  },
  methods: {
    // 提示信息
    notify (message, type) {
      this.$notify({
        title: message,
        type: type
      })
    },
    attachImageUrl (srcUrl) { // 拼接图片地址
      return srcUrl ? this.$store.state.config.HOST + srcUrl : this.$store.state.config.HOST + '/img/user.jpg'
    },
    // 获取生日
    attachBirth (val) {
      if (val) {
        return val.substr(0, 10)
      } else {
        return '未知'
      }
    },
    // 根据歌曲名字模糊查询
    getSong () {
      if (this.$route.query.keywords) {
        likeSongOfName(this.$route.query.keywords).then(res => {
          if (res.length) {
            this.$store.commit('setListOfSongs', res)
          } else {
            this.$store.commit('setListOfSongs', [])
            this.notify('本系统暂时没有该歌曲, 试试别的吧！', 'info')
          }
        }).catch(err => {
          console.log(err)
        })
      } else { // 如果没有输入歌曲名字, 则清空歌曲列表,双保险
        this.$store.commit('setListOfSongs', [])
        this.notify('歌曲名不能为空!', 'warning')
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
