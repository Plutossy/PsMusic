<template>
  <div class="search-song-lists">
    <content-list :contentList="albumDatas"></content-list>
  </div>
</template>

<script>
import ContentList from '@/components/ContentList.vue'
import { getSongListLikeTitle } from '@/api/index.js'
import { mixin } from '@/mixins/index.js'

export default {
  mixins: [mixin],
  name: 'search-song-lists',
  data () {
    return {
      albumDatas: []
    }
  },
  components: {
    ContentList
  },
  mounted () {
    this.getSearchList()
  },
  updated () {
    this.getSearchList()
  },
  methods: {
    async getSearchList () {
      if (!this.$route.query.keywords) {
        this.$message.warning('搜索内容不能为空')
        return
      }
      const res = await getSongListLikeTitle(this.$route.query.keywords)
      if (!res) return this.notify('暂无相关歌单信息', 'warning')
      this.albumDatas = res
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../../assets/css/search-song-lists.scss";
.search-song-lists {
  background: url('../../assets/img/music_bg5.jpg') center top no-repeat;
  background-size: 100% auto;
}
</style>
