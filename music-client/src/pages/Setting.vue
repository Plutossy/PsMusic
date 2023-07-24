<template>
  <div class="setting">
    <div class="leftCol">
      <div class="settingsMainHeader">设置</div>
      <ul class="setting-aside">
        <li v-for="(item, index) in settingList" :key="index" :class="{activeColor: activeName === item.name}" @click="hanfleClick(item)">
          {{ item.name }}
        </li>
      </ul>
    </div>
    <div class="contentCol">
      <component :is="componentSrc"></component>
    </div>
  </div>
</template>

<script>
import Info from '@/components/Info.vue'
import Upload from '@/components/Upload.vue'

export default {
  name: 'setting',
  data () {
    return {
      settingList: [ // 设置列表
        { name: '个人信息', path: 'Info' },
        { name: '修改头像', path: 'Upload' }
      ],
      activeName: '个人信息',
      componentSrc: 'Info'
    }
  },
  components: {
    Info,
    Upload
  },
  mounted () {
    this.$store.commit('setActiveName', '设置')
  },
  methods: {
    hanfleClick (item) {
      this.activeName = item.name
      this.componentSrc = item.path
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/css/setting.scss";
</style>
