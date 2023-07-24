<template>
  <div class="content">
    <h1 class="title">
      <slot name="title"></slot>
      <hr />
    </h1>
    <ul>
      <li>
        <div class="song-item">
          <span class="item-index"></span>
          <span class="item-title" style="font-weight: bold; color: brown;">歌曲名</span>
          <span class="item-name" style="font-weight: bold; color: brown;">歌手</span>
          <span class="item-intro" style="font-weight: bold; color: brown;">专辑</span>
        </div>
      </li>
      <li v-for="(item, index) in songList" :key="index" :class="{'is-play': id == item.id}">
        <div class="song-item" @click="toPlay(item.id, item.url, item.pic, index, item.name, item.lyric)">
          <span class="item-index">{{ index + 1 }}</span>
          <span class="item-title">{{ replaceSong(item.name) }}</span>
          <span class="item-name">{{ replaceName(item.name) }}</span>
          <span class="item-intro">{{ item.introduction }}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from '@/mixins'
import { mapGetters } from 'vuex'

export default {
  mixins: [mixin],
  name: 'album-content',
  props: ['songList'],
  computed: {
    ...mapGetters([
      'id' // 正在播放歌曲id
    ])
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/album-content.scss';
.content {
  background: url(../assets/img/music_bg2.jpg) center top no-repeat;
  background-size: 100% auto;
}
</style>
