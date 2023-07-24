<template>
  <div class="singer-album">
    <div class="album-slide">
      <div class="singer-img animate__animated animate__flip">
        <img :src="attachImageUrl(singer.pic)" alt="">
      </div>
      <ul class="info">
        <li v-if="singer.sex === 0 || singer.sex === 1 || singer.sex === 2 || singer.sex === 3">{{ attachSex(singer.sex) }}</li>
        <li>生日: {{ attachBirth(singer.birth) }}</li>
        <li>地区: {{ singer.location }}</li>
      </ul>
    </div>
    <div class="album-content">
      <div class="intro animate__animated animate__fadeInRight">
        <h2>{{ singer.name }}</h2>
        <span>{{ singer.introduction }}</span>
      </div>
      <div class="content">
        <album-content :songList="listOfSongs">
          <template #title>歌曲列表</template>
        </album-content>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from '@/mixins'
import { mapGetters } from 'vuex'
import { songOfSingerId } from '@/api/index.js'
import AlbumContent from '@/components/AlbumContent.vue'

export default {
  mixins: [mixin],
  name: 'singer-album',
  data () {
    return {
      tempList1: {}, // 当前歌手对象
      singerId: '', // 当前传来的歌单的id
      singer: {}, // 当前歌手对象
      average: 0, // 平均分
      rank: 0 // 提交评价的分数
    }
  },
  components: {
    AlbumContent
  },
  computed: {
    ...mapGetters([
      'listOfSongs', // 当前播放列表
      'tempList', // 当前歌手对象
      'isLogin', // 是否登录
      'userId' // 当前登录用户id
    ])
  },
  created () {
    // 解决刷新页面后，vuex 中的数据丢失的问题
    this.tempList1 = JSON.parse(window.sessionStorage.getItem('tempList'))

    this.singerId = this.$route.params.id
    this.singer = this.tempList1
    this.getSongOfSingerId()
  },
  mounted () {
    this.$store.commit('setActiveName', '歌手')
  },
  methods: {
    // 根据歌手id获取歌曲列表
    getSongOfSingerId () {
      songOfSingerId(this.singerId).then(res => {
        this.$store.commit('setListOfSongs', res)
      }).catch((err) => {
        console.log(err)
        this.$message.error('获取歌曲详情失败, 请稍后重试')
      })
    },
    // 获取性别
    attachSex (val) {
      if (val === 0) {
        return '女'
      } else if (val === 1) {
        return '男'
      } else if (val === 2) {
        return '组合'
      } else {
        return '未知'
      }
    },
    // 获取生日
    attachBirth (val) {
      if (val) {
        return val.substr(0, 10)
      } else {
        return '未知'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/singer-album.scss'
</style>
