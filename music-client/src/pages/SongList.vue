<template>
  <div class="song-list">
    <ul class="song-list-header animate__animated animate__bounceInLeft">
      <li v-for="(item, index) in songStyle" :key="index" @click="handleChangeView(item.name)" :class="{active: item.name===activeName}">{{ item.name }}</li>
    </ul>
    <div class="song-list-content">
      <content-list :contentList="data"></content-list>
      <div class="pagination">
        <el-pagination
          @current-change="handleCurrentChange"
          background
          layout="total, prev, pager, next"
          :current-page="currentPage"
          :page-size="pageSize"
          :total="albumDatas.length"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from '@/mixins/index.js'
import { songStyle } from '@/assets/data/songList.js'
import ContentList from '@/components/ContentList.vue'
import { getAllSongList, getSongListOfStyle } from '@/api/index.js'

export default {
  mixins: [mixin],
  name: 'song-list',
  data () {
    return {
      albumDatas: [], // 歌单数据
      pageSize: 10, // 每页显示的数据条数
      currentPage: 1, // 当前页码
      songStyle: [], // 歌单风格
      activeName: '全部' // 当前选中的歌单风格
    }
  },
  computed: {
    // 计算当前表格中的数据
    data () {
      return this.albumDatas.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  components: {
    ContentList
  },
  mounted () {
    this.songStyle = songStyle
    this.$store.commit('setActiveName', '歌单')
    this.getSongList()
  },
  methods: {
    getSongList () {
      getAllSongList().then(res => {
        this.albumDatas = res
      }).catch(err => {
        console.log(err)
        this.$message.error('获取歌单列表失败')
      })
    },
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    // 根据歌单风格获取歌单
    handleChangeView (name) {
      this.activeName = name
      this.albumDatas = []
      if (name === '全部') {
        this.getSongList()
      } else {
        this.goSongListOfStyle(name)
      }
    },
    // 根据歌单风格查询对应歌单
    goSongListOfStyle (style) {
      getSongListOfStyle(style).then(res => {
        this.currentPage = 1
        this.albumDatas = res
      }).catch(err => {
        console.log(err)
        this.$message.error('获取歌单列表失败')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/song-list.scss";
.song-list-content {
  background: url('../assets/img/music_bg1.jpg') bottom center no-repeat;
  background-size: 100% auto;
}
</style>
