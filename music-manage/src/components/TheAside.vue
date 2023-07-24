<template>
  <div class="sidebar">
    <el-menu class="sider-el-menu" :default-active="onRoutes" :collapse="collapse"
     background-color="#565533" text-color="#ffffff" active-text-color="darkorange" router>
      <template v-for="item in items">
        <template>
          <el-menu-item :index="item.index" :key="item.index">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import bus from '@/assets/js/bus.js'
export default {
  data () {
    return {
      collapse: false,
      items: [
        { icon: 'el-icon-document',
          index: 'info',
          title: '系统首页'
        },
        { icon: 'el-icon-document',
          index: 'consumer',
          title: '用户管理'
        },
        { icon: 'el-icon-document',
          index: 'singer',
          title: '歌手管理'
        },
        { icon: 'el-icon-document',
          index: 'songlist',
          title: '歌单管理'
        }
      ]
    }
  },
  computed: {
    onRoutes () {
      return this.$route.path.replace('/', '')
    }
  },
  created () {
    // 通过eventbus监听折叠事件
    bus.$on('collapse', msg => {
      this.collapse = msg
    })
  }
}
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  background-color: #565533;
  color: #fff;
  overflow: scroll;
}
.sidebar::-webkit-scrollbar {
  width: 0;
}
.sider-el-menu:not(.el-menu--collapse) {
  width: 150px;
}
.sidebar > ul {
  height: 100%;
}
</style>
