<template>
  <div class="my-music">
    <div class="album-slide">
      <div class="album-img animate__animated animate__bounce">
        <img :src="attachImageUrl(avatar)" alt="用户头像">
      </div>
      <ul class="album-info">
        <li>昵称: {{ username }}</li>
        <li>性别: {{ userSex }}</li>
        <li>生日: {{ birth }}</li>
        <li>地区: {{ location }}</li>
      </ul>
    </div>
    <div class="album-content">
      <div class="album-title animate__animated animate__bounce">
        签名: {{ introduction }}
      </div>
      <div class="songs-body">
        <album-content :songList="collectList">
          <template #title>我的收藏</template>
        </album-content>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from '../mixins'
import { mapGetters } from 'vuex'
import { getUserOfId, getCollectOfUserId, songOfSongId } from '@/api/index.js'
import AlbumContent from '@/components/AlbumContent.vue'

export default {
  mixins: [mixin],
  name: 'my-music',
  data () {
    return {
      avatar: '', // 用户头像
      username: '', // 用户名
      userSex: '', // 用户性别
      birth: '', // 用户生日
      location: '', // 用户所在地
      introduction: '', // 用户签名
      collection: [], // 用户收藏歌曲列表
      collectList: [] // 用户收藏列表(歌曲详情)
    }
  },
  computed: {
    ...mapGetters([
      'userId', // 当前登录用户id
      'listOfSongs', // 当前播放歌曲列表
      'isLogin' // 是否登录
    ])
  },
  components: {
    AlbumContent
  },
  mounted () {
    this.$store.commit('setActiveName', '我的音乐')
    if (this.isLogin) {
      this.getMsg(this.userId)
      this.getCollection(this.userId)
    }
  },
  methods: {
    getMsg (userId) {
      getUserOfId(userId).then(res => {
        this.avatar = res.avatar
        this.username = res.username
        if (res.sex === 0) this.userSex = '女'; else this.userSex = '男'
        this.birth = this.attachBirth(res.birth)
        this.location = res.location
        this.introduction = res.introduction
      })
    },
    // 获取我的收藏列表
    getCollection (userId) {
      getCollectOfUserId(userId).then(res => {
        this.collection = res
        // 通过歌曲 id 获取歌曲详情
        this.collection.forEach(item => {
          songOfSongId(item.songId).then(res => {
            this.collectList.push(res)
          }).catch(err => {
            console.log(err)
          })
        })
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/my-music.scss'
</style>
