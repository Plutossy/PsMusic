<template>
  <div class="header">
    <!-- 折叠图片 -->
    <div class="collapse-btn" @click="collapseChange">
      <i class="el-icon-menu"></i>
    </div>
    <div class="logo">PsMusic 后台管理</div>
    <div class="header-right">
      <div class="btn-fullscreen" @click="handleFullScreen">
        <el-tooltip :content="fullscreen ? '取消全屏' : '全屏'" placement="bottom">
          <i class="el-icon-rank"></i>
        </el-tooltip>
      </div>
      <div class="user-avator">
        <img src="../assets/img/user.jpg" alt="管理员头像">
      </div>
      <el-dropdown class="user-name" trigger="click" @command="hadleCommand">
        <span class="el-dropdown-link">
          {{ userName }}
          <i class="el-icon-caret-bottom"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import bus from '@/assets/js/bus.js'
export default {
  data () {
    return {
      collapse: false,
      fullscreen: false
    }
  },
  computed: {
    userName () {
      return sessionStorage.getItem('userName')
    }
  },
  methods: {
    // 折叠事件
    collapseChange () {
      this.collapse = !this.collapse
      bus.$emit('collapse', this.collapse)
    },
    // 全屏事件
    handleFullScreen () {
      if (this.fullscreen) { // 取消全屏
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.mozCancelFullScreen) { // Firefox (works in nightly)
          document.mozCancelFullScreen()
        } else if (document.webkitCancelFullScreen) { // Webkit (works in Safari5.1 and Chrome 15)
          document.webkitCancelFullScreen()
        } else if (document.msExitFullscreen) { // IE11
          document.msExitFullscreen()
        }
      } else { // 全屏
        let element = document.documentElement
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.mozRequestFullScreen) { // Firefox (works in nightly)
          element.mozRequestFullScreen()
        } else if (element.webkitRequestFullScreen) { // Webkit (works in Safari5.1 and Chrome 15)
          element.webkitRequestFullScreen()
        } else if (element.msRequestFullscreen) { // IE11
          element.msRequestFullscreen()
        }
      }
      this.fullscreen = !this.fullscreen
    },
    hadleCommand (command) {
      if (command === 'logout') {
        this.$router.push('/')
        sessionStorage.removeItem('userName')
        localStorage.removeItem('token')
      }
    }
  }
}
</script>

<style scoped>
.header {
  position: relative;
  background-color: #689139;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #ffffff;
}
.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}
.header .logo {
  position: relative;
  float: left;
  line-height: 70px;
}
.header-right {
  float: right;
  padding-right: 50px;
  display: flex;
  height: 70px;
  align-items: center;
}
.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}
.user-avator {
  margin-left: 20px;
}
.user-avator img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.user-name {
  margin-left: 10px;
}
.el-dropdown-link {
  color: #ffffff;
  cursor: pointer;
}
</style>
