<template>
  <div class="singer">
    <ul class="singer-header animate__animated animate__bounceInRight">
      <li v-for="(item, index) in singerStyle" :key="index" @click="handleChangeView(item.type)" :class="{active: item.type===activeName}">{{ item.name }}</li>
    </ul>
    <div class="singer-content">
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
import { singerStyle } from '@/assets/data/singer.js'
import ContentList from '@/components/ContentList.vue'
import { getAllSinger, getSingerOfSex } from '@/api/index.js'

export default {
  mixins: [mixin],
  name: 'singer',
  data () {
    return {
      albumDatas: [], // 歌手数据
      pageSize: 10, // 每页显示的数据条数
      currentPage: 1, // 当前页码
      singerStyle: [], // 风格
      activeName: -1 // 当前选中的歌手, -1表示全部歌手
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
    this.singerStyle = singerStyle
    this.$store.commit('setActiveName', '歌手')
    this.getSingerList()
  },
  methods: {
    getSingerList () {
      getAllSinger().then(res => {
        this.albumDatas = res
      }).catch(err => {
        console.log(err)
        this.$message.error('获取歌手列表失败')
      })
    },
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    // 根据歌手性别获取歌手列表
    handleChangeView (type) {
      this.activeName = type
      this.albumDatas = []
      if (type === -1) {
        this.getSingerList()
      } else {
        this.goSingerOfSex(type)
      }
    },
    // 根据性别查询对应歌手
    goSingerOfSex (sex) {
      getSingerOfSex(sex).then(res => {
        this.currentPage = 1
        this.albumDatas = res
      }).catch(err => {
        console.log(err)
        this.$message.error('获取歌手列表失败')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/singer.scss";
.singer-content {
  background: url('../assets/img/music_bg5.jpg') bottom center no-repeat;
  background-size: 100% auto;
}
</style>
