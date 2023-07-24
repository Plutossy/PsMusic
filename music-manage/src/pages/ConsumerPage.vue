<template>
  <div class="table">
    <div class="container">
      <div class="handle-box">
        <el-button type="danger" @click="delAll" size="mini">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="请输筛选相关用户" class="handle-input"></el-input>
        <el-button type="primary" @click="centerDialogVisible = true" size="mini">添加新用户</el-button>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width: 100%" height="680px" :data="data" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column label="用户图片" width="110" align="center">
        <template slot-scope="scope">
          <div class="consumer-img">
            <img :src="getUrl(scope.row.avatar)" alt="用户图片" width="100%" />
          </div>
          <el-upload :action="uploadUrl(scope.row.id)" :before-upload="beforeAvatarUpload" :on-success="handleAvatarSuccess">
            <el-button size="mini">更新图片</el-button>
          </el-upload>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="120" align="center"></el-table-column>
      <el-table-column label="性别" prop="sex" align="center" width="50">
        <template slot-scope="scope">
          {{ changeSex(scope.row.sex) }}
        </template>
      </el-table-column>
      <el-table-column prop="phoneNum" label="手机号" width="120" align="center"></el-table-column>
      <el-table-column prop="email" label="电子邮箱" width="160" align="center"></el-table-column>
      <el-table-column label="生日" prop="birth" align="center" width="120">
        <template slot-scope="scope">
          {{ attachBirth(scope.row.birth) }}
        </template>
      </el-table-column>
      <el-table-column label="个性签名" prop="introduction" align="center"></el-table-column>
      <el-table-column label="地区" prop="location" align="center" width="80"></el-table-column>
      <el-table-column label="收藏" width="100" align="center">
        <template slot-scope="scope">
          <el-button plain round icon="el-icon-view" type="view" size="mini" @click="getCollect(data[scope.$index].id)">收藏</el-button>
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
    <!-- 添加用户弹出框 -->
    <el-dialog title="添加新用户" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" ref="registerForm" lobel-width="80px" :rules="rules1">
        <el-form-item prop="username" label="用户名" size="mini">
          <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码" size="mini">
          <el-input type="password" v-model="registerForm.password" placeholder="密码" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass" size="mini">
          <el-input type="password" v-model="registerForm.checkPass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <!-- <input type="radio" name="sex" value="0" v-model="registerForm.sex">&nbsp;女&nbsp;&nbsp;
          <input type="radio" name="sex" value="1" v-model="registerForm.sex">&nbsp;男 -->
          <el-radio-group v-model="registerForm.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="phoneNum" label="手机号" size="mini">
          <el-input v-model="registerForm.phoneNum" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="电子邮箱" size="mini">
          <el-input v-model="registerForm.email" placeholder="电子邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="birth" label="生日" size="mini">
          <el-date-picker v-model="registerForm.birth" type="date" placeholder="选择日期" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item prop="introduction" label="个性签名" size="mini">
          <el-input v-model="registerForm.introduction" placeholder="个性签名" type="textarea"></el-input>
        </el-form-item>
        <el-form-item prop="location" label="地区" size="mini">
          <el-input v-model="registerForm.location" placeholder="地区"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible= false">取消</el-button>
        <el-button size="mini" @click="addConsumer">确定</el-button>
      </span>
    </el-dialog>
    <!-- 修改用户弹出框 -->
    <el-dialog title="修改用户" :visible.sync="editVisible" width="400px" center>
      <el-form :model="form" ref="form" lobel-width="80px" :rules="rules2">
        <el-form-item prop="username" label="用户名" size="mini">
          <el-input v-model="form.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码" size="mini">
          <el-input type="password" v-model="form.password" placeholder="密码" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass" size="mini">
          <el-input type="password" v-model="form.checkPass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="性别" size="mini">
          <!-- <input type="radio" name="sex" value="0" v-model="form.sex"> &nbsp;女 &nbsp;&nbsp;&nbsp;&nbsp;
          <input type="radio" name="sex" value="1" v-model="form.sex"> &nbsp;男 -->
          <el-radio-group v-model="form.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="phoneNum" label="手机号" size="mini">
          <el-input v-model="form.phoneNum" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="电子邮箱" size="mini">
          <el-input v-model="form.email" placeholder="电子邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="birth" label="生日" size="mini">
          <el-date-picker v-model="form.birth" type="date" placeholder="选择日期" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item prop="introduction" label="个性签名" size="mini">
          <el-input v-model="form.introduction" placeholder="个性签名" type="textarea"></el-input>
        </el-form-item>
        <el-form-item prop="location" label="地区" size="mini">
          <el-input v-model="form.location" placeholder="地区"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="editVisible= false">取消</el-button>
        <el-button size="mini" @click="editSave">确定</el-button>
      </span>
    </el-dialog>
    <!-- 删除用户弹出框 -->
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
import { getAllConsumer, setConsumer, updateConsumer, delConsumer } from '@/api/index'
import { mixin } from '@/mixins/index'
export default {
  mixins: [ mixin ],
  data () {
    // 添加用户表单密码验证
    var validatePass1 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.registerForm.checkPass !== '') {
          this.$refs.registerForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    // 编辑用户表单密码验证
    var validatePass3 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.form.checkPass !== '') {
          this.$refs.form.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass4 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      centerDialogVisible: false, // 添加用户对话框
      editVisible: false, // 编辑用户对话框
      delVisible: false, // 删除用户对话框
      registerForm: { // 添加用户表单
        username: '',
        password: '',
        checkPass: '',
        sex: 1,
        phoneNum: '',
        email: '',
        birth: '',
        introduction: '',
        location: ''
      },
      form: { // 编辑用户表单
        id: '',
        username: '',
        password: '',
        checkPass: '',
        sex: '',
        phoneNum: '',
        email: '',
        birth: '',
        introduction: '',
        location: ''
      },
      rules1: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
        ],
        password: [
          { validator: validatePass1, required: true, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, required: true, trigger: 'blur' }
        ],
        phoneNum: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { min: 11, max: 11, message: '长度在 11 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        birth: [
          { type: 'date', required: true, message: '请选择生日', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '请输入地区', trigger: 'blur' }
        ]
      },
      rules2: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 15, message: '长度在 2 到 15 个字符', trigger: 'blur' }
        ],
        password: [
          { validator: validatePass3, required: true, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass4, required: true, trigger: 'blur' }
        ],
        phoneNum: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { min: 11, max: 11, message: '长度在 11 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        birth: [
          { required: true, message: '请选择生日', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '请输入地区', trigger: 'blur' }
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
      // 如果搜索框有内容，且用户数据不为空，且临时数据不为空,则返回搜索结果
      if (this.select_word !== '' && this.tableData.length !== 0 && this.tempData.length !== 0) {
        return this.tempData.filter(item => {
          return item.username.indexOf(this.select_word) !== -1 // 返回包含搜索内容的用户
        })
      } else { // 否则分页返回所有用户
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
        // 从所有用户中筛选出包含搜索内容的用户
        this.tableData = this.tempData.filter(item => {
          return item.username.indexOf(val) !== -1 // 返回包含搜索内容的用户
        })
      }
    }
  },
  methods: {
    // 查询所有用户
    getData () {
      this.tableData = []
      this.tempData = []
      getAllConsumer().then(res => {
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
    // 添加用户
    addConsumer () {
      this.$refs['registerForm'].validate(valid => {
        if (valid) {
          // 判断用户是否已经存在，要求严格，用户名、手机号、邮箱都不能重复
          let flag = this.tableData.every(item => this.registerForm.username !== item.username || this.registerForm.phoneNum !== item.phoneNum || this.registerForm.email !== item.email)
          if (flag) {
            // let d = this.registerForm.birth
            let d = new Date(this.registerForm.birth)
            let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
            let params = new URLSearchParams()
            params.append('username', this.registerForm.username)
            params.append('password', this.registerForm.password)
            params.append('sex', this.registerForm.sex)
            params.append('phoneNum', this.registerForm.phoneNum)
            params.append('email', this.registerForm.email)
            params.append('birth', datetime)
            params.append('introduction', this.registerForm.introduction)
            params.append('location', this.registerForm.location)
            params.append('avatar', '/img/consumerAvatar/user.jpg')
            // 发送数据请求到后台
            setConsumer(params).then(res => {
              if (res.code === 1) {
                // 重新获取数据
                this.getData()
                this.notify('添加成功', 'success')
                // 清空表单
                this.registerForm = {
                  username: '',
                  password: '',
                  checkPass: '',
                  sex: '',
                  phoneNum: '',
                  email: '',
                  birth: '',
                  introduction: '',
                  location: ''
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
              username: '',
              password: '',
              checkPass: '',
              sex: '',
              phoneNum: '',
              email: '',
              birth: '',
              introduction: '',
              location: ''
            }
            this.notify('该用户已存在, 请勿重复添加', 'warning')
          }
          this.centerDialogVisible = false
        } else {
          this.notify('请按要求填写表单', 'warning')
          return false
        }
      })
    },
    // 弹出编辑用户框
    handleEdit (row) {
      this.editVisible = true
      this.form = {
        id: row.id,
        username: row.username,
        password: row.password,
        sex: row.sex,
        phoneNum: row.phoneNum,
        email: row.email,
        birth: row.birth,
        introduction: row.introduction,
        location: row.location
      }
    },
    // 编辑用户保存
    editSave () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          let d = new Date(this.form.birth)
          let datetime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
          let params = new URLSearchParams()
          params.append('id', this.form.id)
          params.append('username', this.form.username)
          params.append('password', this.form.password)
          params.append('sex', this.form.sex)
          params.append('phoneNum', this.form.phoneNum)
          params.append('email', this.form.email)
          params.append('birth', datetime)
          params.append('introduction', this.form.introduction)
          params.append('location', this.form.location)
          // 发送数据请求到后台
          updateConsumer(params).then(res => {
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
      return `${this.$store.state.HOST}/consumer/updateConsumerPic?id=${id}`
    },
    // 确定删除当前行用户信息
    deleteRow () {
      delConsumer(this.idx).then(res => {
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
    // 转向该用户的收藏页面
    getCollect (id) {
      this.$router.push({ path: '/collect', query: { id: id } })
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.consumer-img {
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
