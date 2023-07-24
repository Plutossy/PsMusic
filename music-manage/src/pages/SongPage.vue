<template>
  <div class="table">
    <div class="crumbs">
      <i class="el-icon-tickets">&nbsp;歌曲信息</i>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="danger" @click="delAll" size="mini">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="请输入歌曲名" class="handle-input"></el-input>
        <el-button type="primary" @click="centerDialogVisible = true" size="mini">添加歌曲</el-button>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width: 100%" height="680px" :data="data" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column label="歌曲图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="song-img">
            <img :src="getUrl(scope.row.pic)" alt="歌手图片" width="100%" />
          </div>
          <div class="paly" @click="setSongUrl(scope.row.url, scope.row.name)">
            <div v-if="toggle == scope.row.name">
              <svg class="icon">
                <use xlink:href="#icon-zanting"></use>
              </svg>
            </div>
            <div v-else>
              <svg class="icon">
                <use xlink:href="#icon-bofanganniu"></use>
              </svg>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="歌手-歌名" width="120" align="center"></el-table-column>
      <el-table-column prop="introduction" label="专辑" width="150" align="center"></el-table-column>
      <el-table-column label="歌词" align="center">
        <template slot-scope="scope">
          <ul style="height: 100px; overflow-y: scroll;">
            <li v-for="(item, i) in parseLyric(scope.row.lyric)" :key="i">{{ item }}</li>
          </ul>
        </template>
      </el-table-column>
      <el-table-column label="资源更新" align="center" width="110">
        <template slot-scope="scope">
          <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatarUpload" :on-success="handleAvatarSuccess">
            <el-button type="primary" size="mini" style="margin-bottom: 5px !important;">更新图片</el-button>
          </el-upload>
          <el-upload :action="uploadSongUrl(scope.row.id)" :before-upload="beforeSongUpload" :on-success="handleSongSuccess">
            <el-button type="primary" size="mini" style="margin-top: 5px !important;">更新歌曲</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 翻页 -->
    <div class="pagination">
      <el-pagination
      background
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="tableData.length">
      </el-pagination>
    </div>
    <!-- 添加歌曲弹出框 -->
    <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" ref="registerForm" lobel-width="80px" action="" id="tf">
        <div>
          <label for="">歌名</label>
          <el-input type="text" name="name"></el-input>
        </div>
        <div>
          <label for="">专辑</label>
          <el-input type="text" name="introduction"></el-input>
        </div>
        <div>
          <label>歌词</label>
          <el-input type="textarea" name="lyric"></el-input>
        </div>
        <div>
          <label for="">歌曲上传</label><br />
          <input type="file" name="file" class="musicInput">
        </div>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible= false">取消</el-button>
        <el-button size="mini" @click="addSong">确定</el-button>
      </span>
    </el-dialog>
    <!-- 修改歌曲弹出框 -->
    <el-dialog title="修改歌曲" :visible.sync="editVisible" width="400px" center>
      <el-form :model="form" ref="form" lobel-width="80px">
        <el-form-item prop="name" label="歌手-歌名" size="mini">
          <el-input v-model="form.name" placeholder="歌手-歌名"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="专辑" size="mini">
          <el-input v-model="form.introduction" placeholder="专辑"></el-input>
        </el-form-item>
        <el-form-item prop="lyric" label="歌词" size="mini">
          <el-input v-model="form.lyric" placeholder="歌词" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="editVisible= false">取消</el-button>
        <el-button size="mini" @click="editSave">确定</el-button>
      </span>
    </el-dialog>
    <!-- 删除歌曲弹出框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
      <div align="center">此操作将永久删除该歌曲, 是否继续?</div>
      <span slot="footer">
        <el-button size="mini" @click="delVisible= false">取消</el-button>
        <el-button size="mini" @click="deleteRow">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from '@/mixins/index'
import { mapGetters } from 'vuex'
import '@/assets/js/iconfont.js'
import { songOfSingerId, updateSong, delSong } from '@/api/index'

export default {
  mixins: [ mixin ],
  data () {
    return {
      singerId: '', // 歌手id
      singerName: '', // 歌手名
      centerDialogVisible: false, // 添加歌手对话框
      editVisible: false, // 编辑歌手对话框
      delVisible: false, // 删除歌手对话框
      registerForm: { // 添加歌曲表单
        name: '',
        singerName: '',
        introduction: '',
        lyric: ''
      },
      form: { // 编辑歌曲表单
        id: '',
        name: '',
        introduction: '',
        lyric: ''
      },
      tableData: [],
      tempData: [],
      select_word: '',
      pageSize: 5, // 每页显示的条数
      currentPage: 1, // 当前页
      idx: -1, // 当前选择项
      multipleSelection: [], // 当前选择项
      toggle: false // 播放器开关
    }
  },
  created () {
    this.singerId = this.$route.query.id
    this.singerName = this.$route.query.name
    this.getData()
  },
  destroyed () { // 组件销毁时，清除播放器的url
    this.$store.commit('setUrl', '')
    this.$store.commit('setIsPlay', false)
  },
  computed: {
    ...mapGetters([
      'isPlay'
    ]),
    // 计算当前搜索结果表里的数据
    data () {
      // 如果搜索框有内容，且歌手数据不为空，且临时数据不为空,则返回搜索结果
      if (this.select_word !== '' && this.tableData.length !== 0 && this.tempData.length !== 0) {
        return this.tempData.filter(item => {
          return item.name.indexOf(this.select_word) !== -1 // 返回包含搜索内容的歌手
        })
      } else { // 否则分页返回所有歌手
        // slice() 方法可从已有的数组中返回选定的元素
        return this.tableData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
      }
    }
  },
  watch: {
    // 监听搜索框的值
    select_word (val) {
      if (this.select_word === '') {
        this.tableData = this.tempData
      } else {
        this.tableData = []
        // 从所有歌手中筛选出包含搜索内容的歌手
        this.tableData = this.tempData.filter(item => {
          return item.name.indexOf(val) !== -1 // 返回包含搜索内容的歌手
        })
      }
    }
  },
  methods: {
    // 查询所有歌手
    getData () {
      this.tableData = []
      this.tempData = []
      songOfSingerId(this.singerId).then(res => {
        this.tableData = res
        this.tempData = res
        this.currentPage = 1
      }).catch(err => {
        console.log(err)
      })
    },
    // 获取当前页
    handleCurrentChange (val) {
      this.currentPage = val
    },
    // 添加歌曲
    addSong () {
      let _this = this
      let form = new FormData(document.getElementById('tf'))
      form.append('singerId', this.singerId)
      form.set('name', this.singerName + '-' + form.get('name'))
      if (!form.get('lyric')) {
        form.set('lyric', '[00:00.00]暂无歌词')
      }
      let req = new XMLHttpRequest()
      req.onreadystatechange = function () {
        if (req.readyState === 4 && req.status === 200) { // 成功
          let res = JSON.parse(req.responseText)
          if (res.code) {
            // 重新获取数据
            _this.getData()
            // 清空表单
            _this.registerForm = {
              name: '',
              singerName: '',
              introduction: '',
              lyric: ''
            }
            _this.notify(res.msg, 'success')
          } else {
            _this.notify('添加失败', 'error')
          }
        }
      }
      req.open(`post`, `${_this.$store.state.HOST}/song/add`, false)
      req.send(form)
      this.centerDialogVisible = false
    },
    // 编辑歌曲
    handleEdit (row) {
      this.editVisible = true
      this.form = {
        id: row.id,
        name: row.name,
        introduction: row.introduction,
        lyric: row.lyric
      }
    },
    // 编辑歌曲保存
    editSave () {
      // 获取歌手名
      // let array = this.form.name.split('-')
      // console.log(array[0])
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('name', this.form.name)
      params.append('introduction', this.form.introduction)
      params.append('lyric', this.form.lyric)
      // 发送数据请求到后台
      updateSong(params).then(res => {
        if (res.code === 1) {
          // 重新获取数据
          this.getData()
          this.notify('修改成功', 'success')
        } else {
          this.notify('修改失败', 'error')
        }
      }).catch(err => {
        console.log(err)
      })
      this.editVisible = false
    },
    // 更新图片
    uploadUrl (id) {
      return `${this.$store.state.HOST}/song/updateSongPic?id=${id}`
    },
    // 确定删除当前行歌曲信息
    deleteRow () {
      delSong(this.idx).then(res => {
        if (res) {
          // 重新获取数据
          this.getData()
          this.notify('删除成功', 'success')
        } else {
          this.notify('删除失败', 'error')
        }
      }).catch(err => {
        console.log(err)
      })
      this.delVisible = false
    },
    // 解析歌词
    parseLyric (text) {
      let lines = text.split('\n')
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})\]/g
      let result = []
      lines.forEach(item => {
        result.push(item.replace(pattern, '').trim())
      })
      return result
    },
    // 上传歌曲之前的校验
    beforeSongUpload (file) {
      let testMsg = file.name.substring(file.name.lastIndexOf('.') + 1) // lastIndexOf() 方法可返回一个指定的字符串值最后出现的位置，在一个字符串中的指定位置从后向前搜索
      if ((testMsg !== 'mp3') && (testMsg !== 'flac')) {
        this.notify('请上传mp3格式的歌曲', 'error')
        return false
      }
      return true
    },
    // 上传歌曲之后要做的事情
    handleSongSuccess (res, file) {
      let _this = this
      if (res.code === 1) {
        // _this.imageUrl = URL.createObjectURL(file.raw)
        _this.getData() // 重新获取数据
        _this.notify('上传成功', 'success')
      } else {
        _this.notify('上传失败', 'error')
      }
    },
    uploadSongUrl (id) {
      return `${this.$store.state.HOST}/song/updateSongUrl/?id=${id}`
    },
    // 切换播放歌曲
    setSongUrl (url, name) {
      this.toggle = name
      this.$store.commit('setUrl', this.$store.state.HOST + url)
      if (this.isPlay) {
        this.$store.commit('setIsPlay', false)
      } else {
        this.$store.commit('setIsPlay', true)
      }
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.crumbs {
  margin: 20px 10px;
  font-size: 20px;
  font-weight: bold;
}
.song-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}
.handle-input {
  width: 200px;
  margin: 0 20px;
  display: inline-block;
}
.pagination {
  display: flex;
  justify-content: center;
}
.musicInput {
  width: 340px;
  height: 30px;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 5px;
  margin-top: 5px;
}
.paly {
  position: absolute;
  z-index: 999;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  top: 20px;
  left: 18px;
}
.icon {
  width: 2em;
  height: 2em;
  color: rgb(96, 235, 225);
  fill: currentColor;
  overflow: hidden;
}
</style>
