<template>
  <div class="scroll-top" @click="scrollTop" v-if="show">
    <div class="box-in">
      <!-- <svg class="icon">
        <use xlink:href="#icon-arrow-up"></use>
      </svg> -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'scroll-top',
  data () {
    return {
      show: false
    }
  },
  mounted () {
    window.addEventListener('scroll', this.handleScroll)
  },
  methods: {
    scrollTop () {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
      // document.documentElement.scrollTop = document.body.scrollTop = 0
    },
    // 监听滚动事件
    handleScroll () {
      let scrollTop = document.documentElement.scrollTop || document.body.scrollTop
      if (scrollTop > 300) {
        this.show = true
      } else {
        this.show = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/scroll-top.scss'
</style>
