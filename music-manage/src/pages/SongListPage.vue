<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="danger" @click="delAll" size="mini">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="筛选关键字" class="handle-input"></el-input>
        <el-button type="primary" @click="centerDialogVisible = true" size="mini">添加歌单</el-button>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width: 100%" height="680px" :data="data" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column label="歌单图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="songList-img">
            <img :src="getUrl(scope.row.pic)" alt="歌单图片" width="100%" />
          </div>
          <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatarUpload" :on-success="handleAvatarSuccess">
            <el-button size="mini">更新图片</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" width="120" align="center"></el-table-column>
      <el-table-column label="简介" prop="introduction">
        <template slot-scope="scope">
          <p style="height: 100px; overflow: scroll; text-indent: 2em;">{{ scope.row.introduction }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="style" label="风格" width="120" align="center"></el-table-column>
      <el-table-column label="歌曲管理" width="120" align="center">
        <template slot-scope="scope">
          <el-button plain round icon="el-icon-view" type="view" size="mini" @click="songEdit(scope.row.id)">歌曲管理</el-button>
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
    <!-- 添加歌单弹出框 -->
    <el-dialog title="添加歌单" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" ref="registerForm" lobel-width="80px">
        <el-form-item prop="title" label="标题" size="mini">
          <el-input v-model="registerForm.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input v-model="registerForm.introduction" placeholder="简介" type="textarea"></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="registerForm.style" placeholder="风格"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible= false">取消</el-button>
        <el-button size="mini" @click="addSongList">确定</el-button>
      </span>
    </el-dialog>
    <!-- 修改歌单弹出框 -->
    <el-dialog title="修改歌单" :visible.sync="editVisible" width="400px" center>
      <el-form :model="form" ref="form" lobel-width="80px">
        <el-form-item prop="title" label="标题" size="mini">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input v-model="form.introduction" placeholder="简介" type="textarea"></el-input>
        </el-form-item>
        <el-form-item prop="style" label="风格" size="mini">
          <el-input v-model="form.style" placeholder="风格"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="editVisible= false">取消</el-button>
        <el-button size="mini" @click="editSave">确定</el-button>
      </span>
    </el-dialog>
    <!-- 删除歌单弹出框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
      <div align="center">此操作将永久删除该信息, 是否继续?</div>
      <span slot="footer">
        <el-button size="mini" @click="delVisible= false">取消</el-button>
        <el-button size="mini" @click="deleteRow">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getAllSongList, setSongList, updateSongList, delSongList } from '@/api/index'
import { mixin } from '@/mixins/index'
export default {
  mixins: [ mixin ],
  data () {
    return {
      centerDialogVisible: false, // 添加歌单对话框
      editVisible: false, // 编辑歌单对话框
      delVisible: false, // 删除歌单对话框
      registerForm: { // 添加歌单表单
        title: '',
        introduction: '',
        style: ''
      },
      form: { // 编辑歌单表单
        id: '',
        title: '',
        introduction: '',
        style: ''
      },
      tableData: [],
      tempData: [],
      select_word: '',
      pageSize: 5, // 每页显示的条数
      currentPage: 1, // 当前页
      idx: -1, // 当前选择项
      multipleSelection: [] // 当前选择项
    }
  },
  created () {
    this.getData()
  },
  computed: {
    // 计算当前搜索结果表里的数据
    data () {
      // 如果搜索框有内容，且歌单数据不为空，且临时数据不为空,则返回搜索结果
      if (this.select_word !== '' && this.tableData.length !== 0 && this.tempData.length !== 0) {
        return this.tempData.filter(item => {
          return item.title.indexOf(this.select_word) !== -1 // 返回包含搜索内容的歌单
        })
      } else { // 否则分页返回所有歌单
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
        // 从所有歌单中筛选出包含搜索内容的歌单
        this.tableData = this.tempData.filter(item => {
          return item.title.indexOf(val) !== -1 // 返回包含搜索内容的歌单
        })
      }
    }
  },
  methods: {
    // 查询所有歌单
    getData () {
      this.tableData = []
      this.tempData = []
      getAllSongList().then(res => {
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
    // 添加歌单
    addSongList () {
      // 判断歌单是否已经存在
      let flag = this.tableData.every(item => this.registerForm.name !== item.title)
      if (flag) {
        let params = new URLSearchParams()
        params.append('title', this.registerForm.title)
        params.append('pic', '/img/songListPic/123-bf.jpg')
        params.append('introduction', this.registerForm.introduction)
        params.append('style', this.registerForm.style)
        // 发送数据请求到后台
        setSongList(params).then(res => {
          if (res.code === 1) {
          // 重新获取数据
            this.getData()
            this.notify('添加成功', 'success')
            // 清空表单
            this.registerForm = {
              title: '',
              introduction: '',
              style: ''
            }
          } else {
            this.notify('添加失败', 'error')
          }
        }).catch(err => {
          console.log(err)
        })
      } else {
        // 清空表单
        this.registerForm = {
          title: '',
          introduction: '',
          style: ''
        }
        this.notify('歌单已存在, 请勿重复添加', 'warning')
      }
      this.centerDialogVisible = false
    },
    // 编辑歌单
    handleEdit (row) {
      this.editVisible = true
      this.form = {
        id: row.id,
        title: row.title,
        introduction: row.introduction,
        style: row.style
      }
    },
    // 编辑歌单保存
    editSave () {
      let params = new URLSearchParams()
      params.append('id', this.form.id)
      params.append('title', this.form.title)
      params.append('introduction', this.form.introduction)
      params.append('style', this.form.style)
      // 发送数据请求到后台
      updateSongList(params).then(res => {
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
      return `${this.$store.state.HOST}/songList/updateSongListPic?id=${id}`
    },
    // 确定删除当前行歌单信息
    deleteRow () {
      delSongList(this.idx).then(res => {
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
    // 转向歌曲管理页面
    songEdit (id) {
      this.$router.push({
        path: '/listsong',
        query: {
          id: id
        }
      })
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.songList-img {
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
</style>
