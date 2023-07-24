<template>
  <div class="song-list-album">
    <div class="album-slide">
      <div class="album-img animate__animated animate__zoomIn">
        <img :src="attachImageUrl(tempList1.pic)">
      </div>
      <div class="album-info">
        <h2>简介</h2>
        <span>{{ tempList1.introduction }}</span>
      </div>
    </div>
    <div class="album-content">
      <div class="album-title animate__animated animate__fadeInRight">
        <p>{{ tempList1.title }}</p>
      </div>
      <div class="album-score">
        <div>
          <h3>歌单评分: </h3>
          <div class="animate__animated animate__flip">
            <el-rate v-model="average" disabled></el-rate>
          </div>
        </div>
        <span>{{ average * 2 }}</span>
        <div>
          <h3>评价: </h3>
          <div @click="setRank">
            <el-rate v-model="rank" allow-half show-text></el-rate>
          </div>
        </div>
      </div>
      <div class="songs-body">
        <album-content :songList="listOfSongs">
          <template #title>歌曲列表</template>
        </album-content>
        <comment :playId="songListId" :type="1"></comment>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from '@/mixins'
import { mapGetters } from 'vuex'
import { listSongDetail, songOfSongId, setRank, getRankOfSongListId } from '@/api/index.js'
import AlbumContent from '@/components/AlbumContent.vue'
import Comment from '@/components/Comment.vue'

export default {
  mixins: [mixin],
  name: 'song-list-album',
  data () {
    return {
      tempList1: {}, // 当前歌单对象
      songLists: [], // 当前页面需要展示的歌曲列表
      songListId: '', // 当前传来的歌单的id
      average: 0, // 平均分
      rank: 0 // 提交评价的分数
    }
  },
  components: {
    AlbumContent,
    Comment
  },
  computed: {
    ...mapGetters([
      'listOfSongs', // 当前播放列表
      'tempList', // 当前歌单对象
      'isLogin', // 是否登录
      'userId' // 当前登录用户id
    ])
  },
  created () {
    // 解决刷新页面后，vuex 中的数据丢失的问题
    this.tempList1 = JSON.parse(window.sessionStorage.getItem('tempList'))

    this.songListId = this.$route.params.id
    this.getSongList()
    this.getRank(this.songListId)
  },
  mounted () {
    this.$store.commit('setActiveName', '歌单')
  },
  methods: {
    // 获取当前歌单的歌曲列表
    getSongList () {
      listSongDetail(this.songListId).then(res => {
        res.forEach(item => {
          this.getSongDetail(item.songId)
        })
        this.$store.commit('setListOfSongs', this.songLists)
      }).catch((err) => {
        console.log(err)
        this.$message.error('获取歌单详情失败, 请稍后重试')
      })
    },
    // 根据歌曲 id 获取歌曲详情
    getSongDetail (id) {
      songOfSongId(id).then(res => {
        this.songLists.unshift(res)
      }).catch((err) => {
        console.log(err)
        this.$message.error('获取歌曲详情失败, 请稍后重试')
      })
    },
    // 获取歌单评分
    getRank (id) {
      getRankOfSongListId(id).then(res => {
        this.average = res / 2 // 因为有半颗星
      }).catch((err) => {
        console.log(err)
        this.$message.error('获取歌单评分失败, 请稍后重试')
      })
    },
    // 提交评价
    setRank () {
      if (!this.isLogin) {
        this.notify('请先登录再尝试评分吧b(￣▽￣)d', 'warning')
      } else {
        let params = new URLSearchParams()
        params.append('songListId', this.songListId)
        params.append('consumerId', this.userId)
        params.append('score', this.rank * 2)
        setRank(params).then(res => {
          if (res.code === 1) {
            this.notify('恭喜你, ' + res.msg, 'success')
          } else {
            this.$message.error(`很可惜, ${res.msg}`)
          }
        }).catch((err) => {
          console.log(err)
          this.notify('你已经评价过此歌单了, 机会只有一次哦！', 'error')
          return false
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/song-list-album.scss'
</style>
