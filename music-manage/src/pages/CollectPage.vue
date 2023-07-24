<template>
  <div class="table">
    <div class="crumbs">
      <i class="el-icon-tickets">&nbsp;收藏信息</i>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="danger" @click="delAll" size="mini">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="请输入筛选关键字" class="handle-input"></el-input>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width: 100%" height="680px" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="40"></el-table-column>
      <el-table-column prop="name" label="歌手-歌名" align="center"></el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 删除收藏弹出框 -->
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
import { songOfSongId, getCollectOfUserId, delCollect } from '@/api/index'

export default {
  mixins: [ mixin ],
  props: ['id'],
  data () {
    return {
      delVisible: false, // 删除对话框
      tableData: [],
      tempData: [],
      select_word: '',
      idx: -1, // 当前选择项
      multipleSelection: [], // 当前选择项
      songAndName: '' // 拼接歌手名和歌名
    }
  },
  created () {
    this.getData()
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
    // 查询该用户收藏的所有歌曲
    getData () {
      let _this = this
      this.tableData = []
      this.tempData = []
      getCollectOfUserId(this.$route.query.id).then(res => {
        res.forEach(item => {
          _this.getSong(item.songId)
        })
      }).catch(err => {
        console.log(err)
      })
    },
    // 根据歌曲id查询歌曲对象，放到 tempData 和 tableData中
    getSong (id) {
      songOfSongId(id).then(res => {
        this.tempData.push(res)
        this.tableData.push(res)
      }).catch(err => {
        console.log(err)
      })
    },
    // 确定删除当前行歌曲信息
    deleteRow () {
      delCollect(this.$route.query.id, this.idx.id).then(res => {
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
    // 批量删除已经选择的项
    delAll () {
      if (this.multipleSelection.length === 0) {
        this.notify('请至少选择一项', 'warning')
        return
      }
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        for (let item of this.multipleSelection) {
          this.handleDelete(item)
          this.deleteRow()
        }
        this.multipleSelection = []
      })
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
.handle-input {
  width: 200px;
  margin: 0 20px;
  display: inline-block;
}
</style>
