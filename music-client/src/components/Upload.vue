<template>
  <div class="upload">
    <p class="title">修改头像</p>
    <hr />
    <div class="section">
      <el-upload drag :action="uploadUrl()" :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
        <i class="el-icon-upload"></i>
        <div>
          将文件拖到此处, 或<span style="color: rgb(102, 102, 209);">点击修改头像</span>
        </div>
      </el-upload>
    </div>
    <div slot="tip" class="tips">只能上传jpg/png文件, 且不能超过 10 MB</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { mixin } from '@/mixins/index.js'

export default {
  name: 'upload',
  mixins: [mixin],
  data () {
    return {

    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  methods: {
    uploadUrl () {
      return `${this.$store.state.config.HOST}/consumer/updateConsumerPic?id=${this.userId}`
    },
    handleAvatarSuccess (res, file) {
      if (res.code === 1) {
        this.$store.commit('setAvatar', res.avatar)
        this.notify('头像修改成功', 'success')
      } else {
        this.messge.error('头像修改失败')
      }
    },
    // 上传之前的校验
    beforeAvatarUpload (file) {
      const isJPG = (file.type === 'image/jpeg') || (file.type === 'image/png') || (file.type === 'image/gif')
      const isLt10M = (file.size / 1024 / 1024) < 10
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 或者 png 格式')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传头像图片大小不能超过 10MB')
        return false
      }
      return true
    }
  }
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/upload.scss";
</style>
