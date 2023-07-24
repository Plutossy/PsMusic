<template>
  <div class="the-footer">
    <p>关于 | 帮助 | 条款 | 反馈</p>
    <p>Copyright&nbsp;&copy;2023-7-20&nbsp;Designed by Pluto_ssy</p>
    <div style="color: #F08047;margin-top: -10px;">
      <span v-html="timeDay"></span>
      <span v-html="timeDate"></span>
      (*๓´╰╯`๓)
    </div>
    <div style="margin-top: 4px;">
      <a href="javascript:;" style="text-decoration: none;">赣ICP备20004408号-1</a>
    </div>
  </div>
</template>

<script>
export default {
  name: 'the-footer',
  data () {
    return {
      timeDay: '',
      timeDate: ''
    }
  },
  created () {
    this.createtime()
    setInterval(this.createtime, 1000)
  },
  methods: {
    createtime () {
      var now = new Date() // 当前时间
      var grt = new Date('07/20/2023 18:00:00')// 在此处修改你的建站时间，格式：月/日/年 时:分:秒
      now.setTime(now.getTime() + 250)
      let days = (now - grt) / 1000 / 60 / 60 / 24
      let dnum = Math.floor(days)
      let hours = (now - grt) / 1000 / 60 / 60 - (24 * dnum)
      let hnum = Math.floor(hours)
      if (String(hnum).length === 1) {
        hnum = '0' + hnum
      }
      let minutes = (now - grt) / 1000 / 60 - (24 * 60 * dnum) - (60 * hnum)
      let mnum = Math.floor(minutes)
      if (String(mnum).length === 1) {
        mnum = '0' + mnum
      }
      let seconds = (now - grt) / 1000 - (24 * 60 * 60 * dnum) - (60 * 60 * hnum) - (60 * mnum)
      let snum = Math.round(seconds)
      if (String(snum).length === 1) {
        snum = '0' + snum
      }
      this.timeDay = '我的 PS_MUSIC 已演唱 ' + dnum + ' 天 '
      this.timeDate = hnum + ' 时 ' + mnum + ' 分 ' + snum + ' 秒'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/the-footer.scss';
.the-footer {
  background: url(../assets/img/music_bg4.jpg) bottom center no-repeat;
  background-size: 100% auto;
}
</style>
