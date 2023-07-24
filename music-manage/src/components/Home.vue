<template>
  <div>
    <the-header></the-header>
    <the-aside></the-aside>
    <div class="content-box" :class="{'content-collapse': collapse}">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
import TheAside from './TheAside.vue'
import TheHeader from './TheHeader.vue'
import bus from '@/assets/js/bus.js'
export default {
  data () {
    return {
      collapse: false
    }
  },
  created () {
    // 通过eventbus监听折叠事件
    bus.$on('collapse', msg => {
      this.collapse = msg
    })
  },
  components: {
    TheAside,
    TheHeader
  }
}
</script>
