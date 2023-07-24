<template>
  <div class="song-audio">
    <audio ref="player"
      :src="url"
      controls = "controls"
      preload = "true"
      @canplay="startPlay"
      @ended="ended"
    ></audio>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'song-audio',
  computed: {
    ...mapGetters([
      'id',
      'url',
      'isPlay'
    ])
  },
  watch: {
    // 监听播放还是暂停
    isPlay () {
      this.togglePlay()
    }
  },
  methods: {
    // 获取链接后准备播放
    startPlay () {
      // 开始播放
      this.$refs.player.play()
    },
    // 播放完成之后触发
    ended () {
      this.isPlay = false
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
    }
  }
}
</script>

<style scoped>
.song-audio {
  display: none;
}
</style>
