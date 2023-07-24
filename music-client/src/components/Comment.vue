<template>
  <div>
    <div class="comment">
      <h2>评论</h2>
      <div class="comment-msg">
        <div class="comment-img">
          <img :src="attachImageUrl(avatar)" alt="用户头像">
        </div>
          <el-input
            class="comment-input"
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="textarea"
            maxlength="200"
            show-word-limit
          ></el-input>
      </div>
      <el-button type="primary" class="sub-btn" @click="postComment">评论</el-button>
    </div>
    <p>精彩评论: 共{{ commentList.length }}条</p>
    <hr />
    <div class="bgiPopular">
      <ul class="popular" v-for="(item, index) in commentList" :key="index">
        <li>
          <div class="popular-img">
            <img :src="attachImageUrl(userPic[index])">
          </div>
          <div class="popular-msg">
            <ul>
              <li class="name">{{ userName[index] }}</li>
              <li class="time">{{ item.createTime }}</li>
              <li class="content">{{ item.content }}</li>
            </ul>
          </div>
          <div class="up" ref="up" @click="isUp(item.id, item.up, index)" onselectstart="return false">
            <svg class="icon" onselectstart="return false">
              <use xlink:href="#icon-zan"></use>
            </svg>
            {{ item.up }}
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mixin } from '@/mixins'
import { mapGetters } from 'vuex'
import { setComment, getAllComment, getUserOfId, setLike, addCommentUp, deleteCommentUp } from '@/api/index.js'

export default {
  mixins: [mixin],
  name: 'comment',
  data () {
    return {
      textarea: '', // 评论内容
      commentList: [], // 评论列表
      userPic: [], // 用户头像
      userName: [], // 用户名
      twoClick: true // 判断点赞或取消点赞
      // token: '' // 判断是否是当前登录用户
    }
  },
  props: [
    'playId', // 歌曲或歌单的id
    'type' // 评论的类型，0歌曲，1歌单
  ],
  computed: {
    ...mapGetters([
      'id', // 当前歌曲或歌单的id
      'isLogin', // 是否登录
      'userId', // 当前登录用户id
      'avatar' // 当前登录用户头像
    ])
  },
  mounted () {
    this.getComment()
  },
  methods: {
    // 提交评论
    postComment () {
      if (!this.isLogin) {
        this.notify('想的还怪多的，先登录吧！', 'warning')
      } else {
        let params = new URLSearchParams()
        if (this.type === 0) {
          params.append('songId', this.playId)
        } else {
          params.append('songListId', this.playId)
        }
        params.append('userId', this.userId)
        params.append('type', this.type)
        params.append('content', this.textarea)
        setComment(params).then(res => {
          if (res.code === 1) {
            this.notify('评论成功', 'success')
            this.textarea = ''
            this.getComment()
          } else {
            this.notify(res.msg, 'error')
          }
        }).catch(err => {
          console.log(err)
          this.$message.error('评论失败, 请稍后重试')
        })
      }
    },
    // 评论列表
    getComment () {
      getAllComment(this.type, this.playId).then(res => {
        this.commentList = res.reverse()
        res.forEach(item => {
          this.getUsers(item.userId)
        })
      }).catch(err => {
        console.log(err)
        this.$message.error('评论加载失败, 请稍后重试')
      })
    },
    // 获取用户头像和昵称
    getUsers (id) {
      // this.userPic = []
      // this.userName = []
      getUserOfId(id).then(res => {
        this.userPic.push(res.avatar)
        this.userName.push(res.username)
        this.userPic.reverse()
        this.userName.reverse()
      }).catch(err => {
        console.log(err)
        this.$message.error('获取用户头像和昵称失败, 请稍后重试')
      })
    },
    // 点赞
    postUp (id, up, index) {
      let params1 = new URLSearchParams()
      params1.append('id', id)
      params1.append('up', up + 1)
      setLike(params1).then(res => {
        if (res.code === 1) {
          let params2 = new URLSearchParams()
          params2.append('userId', this.userId)
          params2.append('commentId', id)
          addCommentUp(params2).then(res => {
            if (res.code === 1) {
              this.$refs.up[index].children[0].style.color = '#ff0000'
              this.commentList[index].up = up + 1
              this.twoClick = false
            } else {
              this.$message.error('点赞失败, 请稍后重试')
            }
          }).catch(err => {
            console.log(err)
            this.notify('你已经点过赞了，休息一下吧', 'warning')
            this.twoClick = true
            return false
          })
        }
      }).catch(err => {
        console.log(err)
        this.$message.error('点赞失败, 请稍后重试')
      })
    },
    // 取消点赞
    postDown (id, up, index) {
      let params = new URLSearchParams()
      params.append('id', id)
      params.append('up', up - 1)
      setLike(params).then(res => {
        if (res.code === 1) {
          deleteCommentUp(this.userId, id).then(() => {
            this.$refs.up[index].children[0].style.color = '#000000'
            this.commentList[index].up = up - 1
            this.twoClick = true
          }).catch(err => {
            console.log(err)
            this.notify('不能再点了，我真的生气了', 'warning')
            this.twoClick = true
            return false
          })
        }
      }).catch(err => {
        console.log(err)
        this.$message.error('取消点赞失败, 请稍后重试')
      })
    },
    // 判断是否点赞
    isUp (id, up, index) {
      if (this.twoClick) {
        if (!this.isLogin) {
          this.notify('想的还怪多的，先登录吧！', 'warning')
        } else {
          // 点赞
          this.postUp(id, up, index)
        }
      } else {
        if (!this.isLogin) {
          this.notify('想的还怪多的，先登录吧！', 'warning')
        } else {
          // 取消点赞
          this.postDown(id, up, index)
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/comment.scss";
.bgiPopular {
  color: rgba(24, 153, 33, 0.887);
  background: url(../assets/img/music_bg7.jpg) top center no-repeat;
  background-size: 100% auto;
}
</style>
