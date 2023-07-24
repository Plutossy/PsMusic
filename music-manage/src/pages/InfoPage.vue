<template>
  <div>
    <el-row :gutter="20" class="mgb20">
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ consumerCount }}</div>
              <div>用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ songCount }}</div>
              <div>歌曲总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ singerCount }}</div>
              <div>歌手数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="grid-content">
            <div class="grid-cont-center">
              <div class="grid-num">{{ songListCount }}</div>
              <div>歌单数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 统计 -->
    <el-row :gutter="20" class="mgb20">
    <!-- 统计用户性别比例图 -->
      <el-col :span="12">
        <h3 class="grid-title">用户性别比例</h3>
        <div style="background-color: white;">
          <ve-pie :data="consumerSex" :theme="options"></ve-pie>
        </div>
      </el-col>
      <!-- 统计歌曲类型 -->
      <el-col :span="12">
        <h3 class="grid-title">歌曲类型分布</h3>
        <div style="background-color: white;">
          <ve-histogram :data="songStyle"></ve-histogram>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" class="mgb20">
    <!-- 统计用户性别比例图 -->
      <el-col :span="12">
        <h3 class="grid-title">歌手性别比例</h3>
        <div style="background-color: white;">
          <ve-pie :data="singerSex"></ve-pie>
        </div>
      </el-col>
      <!-- 统计歌曲类型 -->
      <el-col :span="12">
        <h3 class="grid-title">歌手国籍分布</h3>
        <div style="background-color: white;">
          <ve-histogram :data="country" :theme="options1"></ve-histogram>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAllConsumer, getAllSong, getAllSinger, getAllSongList, listSongDetail } from '@/api/index'

export default {
  data () {
    return {
      consumerCount: 0, // 用户总数
      songCount: 0, // 歌曲总数
      singerCount: 0, // 歌手数量
      songListCount: 0, // 歌单数量
      consumer: [], // 所有用户
      singer: [], // 所有歌手
      consumerSex: { // 按性别分类的用户数
        columns: ['性别', '总数'],
        rows: [
          { '性别': '男', '总数': 0 },
          { '性别': '女', '总数': 0 }
        ]
      },
      options: { // 图表主题
        color: ['#87cefa', '#ffc0cb']
      },
      options1: {
        color: ['yellow']
      },
      songStyle: { // 按歌单风格进行分类
        columns: ['风格', '总数'],
        rows: [
          { '风格': '经典', '总数': 0 },
          { '风格': '乐器', '总数': 0 },
          { '风格': '电子', '总数': 0 },
          { '风格': '说唱', '总数': 0 },
          { '风格': '民谣', '总数': 0 },
          { '风格': '摇滚', '总数': 0 },
          { '风格': '爵士', '总数': 0 },
          { '风格': '古典', '总数': 0 },
          { '风格': '蓝调', '总数': 0 },
          { '风格': '嘻哈', '总数': 0 },
          { '风格': '拉丁', '总数': 0 },
          { '风格': '古风', '总数': 0 },
          { '风格': '华语', '总数': 0 },
          { '风格': '粤语', '总数': 0 },
          { '风格': '欧美', '总数': 0 },
          { '风格': '日韩', '总数': 0 },
          { '风格': 'BGM', '总数': 0 },
          { '风格': '其他', '总数': 0 }
        ]
      },
      singerSex: { // 按性别分类的用户数
        columns: ['性别', '总数'],
        rows: [
          { '性别': '男', '总数': 0 },
          { '性别': '女', '总数': 0 },
          { '性别': '组合', '总数': 0 },
          { '性别': '不明', '总数': 0 }
        ]
      },
      country: { // 按歌手国籍进行分类
        columns: ['国籍', '总数'],
        rows: [
          { '国籍': '中国', '总数': 0 },
          { '国籍': '日本', '总数': 0 },
          { '国籍': '韩国', '总数': 0 },
          { '国籍': '美国', '总数': 0 },
          { '国籍': '英国', '总数': 0 },
          { '国籍': '法国', '总数': 0 },
          { '国籍': '德国', '总数': 0 },
          { '国籍': '意大利', '总数': 0 },
          { '国籍': '印度', '总数': 0 },
          { '国籍': '其他', '总数': 0 }
        ]
      }
    }
  },
  // 页面加载时获取数据
  mounted () {
    this.getConsumerCount()
    this.getSongCount()
    this.getSingerCount()
    this.getSongListCount()
  },
  methods: {
    // 获取用户总数
    async getConsumerCount () {
      const res = await getAllConsumer()
      this.consumer = res
      this.consumerCount = res.length
      this.consumerSex.rows[0]['总数'] = this.setSex(1, this.consumer)
      this.consumerSex.rows[1]['总数'] = this.setSex(0, this.consumer)
    },
    // 根据性别获取用户数
    setSex (sex, val) {
      let count = 0
      val.forEach(item => {
        if (sex === item.sex) {
          count++
        }
      })
      return count
    },
    // 获取歌曲总数
    async getSongCount () {
      const res = await getAllSong()
      this.songCount = res.length
    },
    // 获取歌手数量
    async getSingerCount () {
      const res = await getAllSinger()
      this.singerCount = res.length
      this.singer = res
      this.singerSex.rows[0]['总数'] = this.setSex(1, this.singer)
      this.singerSex.rows[1]['总数'] = this.setSex(0, this.singer)
      this.singerSex.rows[2]['总数'] = this.setSex(2, this.singer)
      this.singerSex.rows[3]['总数'] = this.setSex(3, this.singer)
      // 获取歌手国籍
      this.singer.forEach(item => {
        this.getByCountry(item.location)
      })
    },
    // 根据歌手国籍获取歌手数量
    getByCountry (location) {
      this.country.rows.forEach(row => {
        if (location.includes(row['国籍'])) {
          row['总数']++
        }
      })
    },
    // 获取歌单数量
    async getSongListCount () {
      const res = await getAllSongList()
      this.songListCount = res.length

      res.forEach(item => { // 获取歌单中style字段
        // 获取歌单中歌曲数量,并调用getByStyle方法
        listSongDetail(item.id).then(res => {
          this.getByStyle(item.style, res.length)
        })
      })
    },
    // 根据歌单风格获取歌曲数量
    getByStyle (style, listsongCount) {
      this.songStyle.rows.forEach(row => {
        if (style.includes(row['风格'])) {
          row['总数'] = listsongCount
        }
      })
    }
  }
}
</script>

<style scoped>
.grid-content {
  height: 50px;
  display: flex;
  align-items: center;
}
.grid-cont-center {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: darkgray;
}
.grid-num {
  font-size: 30px;
  font-weight: bold;
}
.grid-title {
  text-align: center;
  margin-bottom: 10px;
  color: rgb(160, 142, 142);
}
</style>
