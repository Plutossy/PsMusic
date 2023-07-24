<template>
  <div class="song-lyric animate__animated animate__fadeInUp">
    <h1 class="lyric-title">歌词</h1>
    <!-- 有歌词 -->
    <ul class="has-lyric" v-if="lyr.length" key="index">
      <li ref="hLyric" v-for="(item, index) in lyr" :key="index">
        {{ item[1] }}
      </li>
    </ul>
    <!-- 没有歌词 -->
    <div v-else class="no-lyric" key="no-lyric">
      <span>Pluto 正在努力加载，先听音乐吧！</span>
    </div>
  </div>
</template>

<script>
import { mixin } from '@/mixins/index.js'
import { mapGetters } from 'vuex'

export default {
  mixins: [mixin],
  name: 'Lyric',
  data () {
    return {
      lyr: []
    }
  },
  computed: {
    ...mapGetters([
      'curTime', // 当前播放时间
      'id', // 正在播放歌曲id
      'lyric', // 歌词
      'listOfSongs', // 当前歌曲列表
      'listIndex' // 当前歌曲在列表中的位置
    ])
  },
  created () {
    this.lyr = this.lyric
  },
  watch: {
    id: function () {
      this.lyr = this.parseLyric(this.listOfSongs[this.listIndex].lyric)
    },
    curTime: function () {
      if (this.lyr.length > 0) {
        for (let i = 0; i < this.lyr.length; i++) {
          if (this.curTime > this.lyr[i][0]) {
            for (let j = 0; j < this.lyr.length; j++) {
              this.$refs.hLyric[j].style.color = '#000'
              this.$refs.hLyric[j].style.fontSize = '15px'
            }
            if (i >= 0) {
              for (let m = 0; m < i; m++) {
                this.$refs.hLyric[m].style.color = 'grey'
                this.$refs.hLyric[m].style.fontSize = '15px'
              }
              this.$refs.hLyric[i].style.color = 'green'
              this.$refs.hLyric[i].style.fontSize = '25px'
            }
          }
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/lyric.scss";
</style>
