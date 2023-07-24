<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="danger" @click="delAll" size="mini">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="请输入歌手名" class="handle-input"></el-input>
        <el-button type="primary" @click="centerDialogVisible = true" size="mini">添加歌手</el-button>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width: 100%" height="680px" :data="data" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column label="歌手图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="singer-img">
            <img :src="getUrl(scope.row.pic)" alt="歌手图片" width="100%" />
          </div>
          <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatarUpload" :on-success="handleAvatarSuccess">
            <el-button size="mini">更新图片</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="歌手" width="110" align="center"></el-table-column>
      <el-table-column label="性别" prop="sex" align="center" width="50">
        <template slot-scope="scope">
          {{ changeSex(scope.row.sex) }}
        </template>
      </el-table-column>
      <el-table-column label="生日" prop="birth" align="center" width="110">
        <template slot-scope="scope">
          {{ attachBirth(scope.row.birth) }}
        </template>
      </el-table-column>
      <el-table-column label="地区" prop="location" align="center" width="100"></el-table-column>
      <el-table-column label="简介" prop="introduction">
        <template slot-scope="scope">
          <p style="height: 100px; overflow: scroll; text-indent: 2em;">{{ scope.row.introduction }}</p>
        </template>
      </el-table-column>
      <el-table-column label="歌曲管理" width="120" align="center">
        <template slot-scope="scope">
          <el-button plain round icon="el-icon-view" type="view" size="mini" @click="songEdit(scope.row.id, scope.row.name)">歌曲管理</el-button>
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
    <!-- 添加歌手弹出框 -->
    <el-dialog title="添加歌手" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" ref="registerForm" lobel-width="80px" :rules="rules">
        <el-form-item prop="name" label="歌手名" size="mini">
          <el-input v-model="registerForm.name" placeholder="歌手名"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">组合</el-radio>
            <el-radio :label="3">不明</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="birth" label="生日" size="mini">
          <el-date-picker v-model="registerForm.birth" type="date" placeholder="选择日期" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item prop="location" label="地区" size="mini">
          <el-input v-model="registerForm.location" placeholder="地区"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input v-model="registerForm.introduction" placeholder="简介" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible= false">取消</el-button>
        <el-button size="mini" @click="addSinger">确定</el-button>
      </span>
    </el-dialog>
    <!-- 修改歌手弹出框 -->
    <el-dialog title="修改歌手" :visible.sync="editVisible" width="400px" center>
      <el-form :model="form" ref="form" lobel-width="80px" :rules="rules1">
        <el-form-item prop="name" label="歌手名" size="mini">
          <el-input v-model="form.name" placeholder="歌手名"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">组合</el-radio>
            <el-radio :label="3">不明</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日" size="mini">
          <el-date-picker v-model="form.birth" type="date" placeholder="选择日期" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item prop="location" label="地区" size="mini">
          <el-input v-model="form.location" placeholder="地区"></el-input>
        </el-form-item>
        <el-form-item prop="introduction" label="简介" size="mini">
          <el-input v-model="form.introduction" placeholder="简介" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="editVisible= false">取消</el-button>
        <el-button size="mini" @click="editSave">确定</el-button>
      </span>
    </el-dialog>
    <!-- 删除歌手弹出框 -->
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
import { setSinger, getAllSinger, updateSinger, delSinger } from '@/api/index'
import { mixin } from '@/mixins/index'
export default {
  mixins: [ mixin ],
  data () {
    return {
      centerDialogVisible: false, // 添加歌手对话框
      editVisible: false, // 编辑歌手对话框
      delVisible: false, // 删除歌手对话框
      registerForm: { // 添加歌手表单
        name: '',
        sex: 1,
        birth: '',
        location: '',
        introduction: ''
      },
      form: { // 编辑歌手表单
        id: '',
        name: '',
        sex: '',
        birth: '',
        location: '',
        introduction: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入歌手名字', trigger: 'blur' }
        ],
        birth: [
          { type: 'date', required: true, message: '请选择出生日期', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '请输入歌手地区', trigger: 'blur' }
        ],
        introduction: [
          { required: true, message: '请输入歌手简介', trigger: 'blur' }
        ]
      },
      rules1: {
        name: [
          { required: true, message: '请输入歌手名字', trigger: 'blur' }
        ],
        birth: [
          { required: true, message: '请选择出生日期', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '请输入歌手地区', trigger: 'blur' }
        ],
        introduction: [
          { required: true, message: '请输入歌手简介', trigger: 'blur' }
        ]
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
      getAllSinger().then(res => {
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
    // 添加歌手
    addSinger () {
      this.$refs['registerForm'].validate(valid => {
        if (valid) {
          // 判断歌手是否已经存在
          let flag = this.tableData.every(item => this.registerForm.name !== item.name)
          if (flag) {
            // let d = this.registerForm.birth
            let d = new Date(this.registerForm.birth)
            let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
            let params = new URLSearchParams()
            params.append('name', this.registerForm.name)
            params.append('sex', this.registerForm.sex)
            params.append('pic', '/img/singerPic/hhh.jpg')
            params.append('birth', datetime)
            params.append('location', this.registerForm.location)
            params.append('introduction', this.registerForm.introduction)
            // 发送数据请求到后台
            setSinger(params).then(res => {
              if (res.code === 1) {
                // 重新获取数据
                this.getData()
                this.notify('添加成功', 'success')
                // 清空表单
                this.registerForm = {
                  name: '',
                  sex: '',
                  birth: '',
                  location: '',
                  introduction: ''
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
              name: '',
              sex: '',
              birth: '',
              location: '',
              introduction: ''
            }
            this.notify('歌手已存在, 请勿重复添加', 'warning')
          }
          this.centerDialogVisible = false
        } else {
          this.notify('请按要求填写表单', 'warning')
          return false
        }
      })
    },
    // 弹出编辑歌手框
    handleEdit (row) {
      this.editVisible = true
      this.form = {
        id: row.id,
        name: row.name,
        sex: row.sex,
        birth: row.birth,
        location: row.location,
        introduction: row.introduction
      }
    },
    // 编辑歌手保存
    editSave () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          let d = new Date(this.form.birth)
          let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
          let params = new URLSearchParams()
          params.append('id', this.form.id)
          params.append('name', this.form.name)
          params.append('sex', this.form.sex)
          params.append('birth', datetime)
          params.append('location', this.form.location)
          params.append('introduction', this.form.introduction)
          // 发送数据请求到后台
          updateSinger(params).then(res => {
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
        } else {
          this.notify('请按要求填写表单', 'warning')
          return false
        }
      })
    },
    // 更新图片
    uploadUrl (id) {
      return `${this.$store.state.HOST}/singer/updateSingerPic?id=${id}`
    },
    // 确定删除当前行歌手信息
    deleteRow () {
      delSinger(this.idx).then(res => {
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
    songEdit (id, name) {
      this.$router.push({
        path: '/song',
        query: {
          id: id,
          name: name
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
.singer-img {
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
