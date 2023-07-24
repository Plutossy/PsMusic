<template>
  <div class="search">
    <nav class="searchList-nav" ref="change">
      <span :class="{isActive: toggle == 'Songs'}" @click="handleChangeView('Songs')">歌曲</span>
      <span :class="{isActive: toggle == 'SongLists'}" @click="handleChangeView('SongLists')">歌单</span>
    </nav>
    <compontent :is="currentView"></compontent>
  </div>
</template>

<script>
import SearchSongs from '@/components/search/SearchSongs'
import SearchSongLists from '@/components/search/SearchSongLists'

export default {
  name: 'search',
  components: {
    SearchSongs,
    SearchSongLists
  },
  data () {
    return {
      toggle: 'Songs',
      currentView: 'SearchSongs'
    }
  },
  mounted () {
    this.$store.commit('setActiveName', '搜索')
  },
  methods: {
    // 切换组件视图
    handleChangeView (view) {
      this.toggle = view
      this.currentView = 'Search' + view
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/search.scss'
</style>
