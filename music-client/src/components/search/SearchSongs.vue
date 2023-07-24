<template>
  <div class="search-songs">
    <album-content :songList="listOfSongs"></album-content>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { mixin } from '@/mixins/index.js'
import AlbumContent from '@/components/AlbumContent'

export default {
  name: 'search-songs',
  mixins: [mixin],
  computed: {
    ...mapGetters([
      'listOfSongs' // 当前歌曲列表
    ])
  },
  components: {
    AlbumContent
  }
}
</script>

<style lang="scss" scoped>
@import '../../assets/css/search-songs.scss';
</style>
