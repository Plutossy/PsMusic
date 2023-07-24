<template>
  <div class="content-list">
    <ul class="section-content">
      <li class="content-item animate__animated animate__flipInX" v-for="(item, index) in contentList" :key="index">
        <div class="kuo" @click="goAlbum(item, item.name)">
          <img :src="attachImageUrl(item.pic)" class="item-img">
          <div class="mask">
            <svg class="icon">
              <use xlink:href="#icon-bofang"></use>
            </svg>
          </div>
        </div>
        <p class="item-name">{{ item.name || item.title }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { mixin } from '@/mixins'

export default {
  mixins: [mixin],
  name: 'content-list',
  props: ['contentList'],
  data () {
    return {
      type: ''
    }
  },
  methods: {
    goAlbum (item, type) {
      this.$store.commit('setTempList', item)
      if (type) { // 歌手
        this.$router.push(`/singer-album/${item.id}`)
        this.$store.commit('setActiveName', '歌手')
      } else { // 歌单
        this.$router.push(`/song-list-album/${item.id}`)
        this.$store.commit('setActiveName', '歌单')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/content-list.scss';
</style>
