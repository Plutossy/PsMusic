<template>
  <div>
    <div class="info">
      <div class="title">
        <span>编辑个人资料</span>
      </div>
      <hr />
      <div class="personal">
        <el-form :model="registerForm" ref="registerForm" label-width="80px" class="demo-ruleForm" :rules="rules">
          <el-form-item prop="username" label="用户名">
            <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item prop="password" label="密码">
            <el-input type="password" v-model="registerForm.password" placeholder="密码" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="password" v-model="registerForm.checkPass" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item prop="sex" label="性别">
            <el-radio-group v-model="registerForm.sex">
              <el-radio :label="0">女</el-radio>
              <el-radio :label="1">男</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="phoneNum" label="手机">
            <el-input v-model="registerForm.phoneNum" placeholder="手机"></el-input>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
          </el-form-item>
          <el-form-item prop="birth" label="生日">
            <el-date-picker v-model="registerForm.birth" type="date" placeholder="选择日期" style="width: 100%;"></el-date-picker>
          </el-form-item>
          <el-form-item prop="introduction" label="签名">
            <el-input v-model="registerForm.introduction" placeholder="签名"></el-input>
          </el-form-item>
          <el-form-item prop="location" label="地区">
            <el-select v-model="registerForm.location" placeholder="请选择" style="width: 100%;">
              <el-option
                v-for="item in cities"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class="btn">
          <div type="primary" @click="saveMsg()">保存</div>
          <div @click="goback(-1)">取消</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from '@/mixins/index.js'
import { mapGetters } from 'vuex'
import { cities } from '@/assets/data/form.js'
import { getUserOfId, updateUserMsg, getAllConsumer } from '@/api/index.js'

export default {
  mixins: [mixin],
  name: 'info',
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
    return {
      registerForm: {
        username: '', // 用户名
        password: '', // 密码
        checkPass: '', // 确认密码
        sex: 1, // 性别
        phoneNum: '', // 手机
        email: '', // 邮箱
        birth: '', // 生日
        introduction: '', // 签名
        location: '', // 所在地
        tableData: [] // 所有用户
      },
      cities: [], // 所有地区
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]{2,16}$/, message: '用户名必须为2-16位, 不含特殊字符', trigger: ['blur', 'change'] }
        ],
        password: [
          { validator: validatePass1, required: true, message: '请输入密码', trigger: 'blur' },
          { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{6,16}$/, message: '至少6-16个字符，至少1个大写字母，1个小写字母和1个数字，其他可以是任意字符', trigger: ['blur', 'change'] }
        ],
        checkPass: [
          { validator: validatePass2, required: true, trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        phoneNum: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号', trigger: ['blur', 'change'] }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        birth: [
          { required: true, message: '请选择日期', trigger: 'change' }
        ],
        introduction: [
          { message: '请输入介绍', trigger: 'blur' }
        ],
        location: [
          { message: '请输入地区', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'userId', // 当前登录用户id
      'isLogin' // 当前登录状态
    ])
  },
  created () {
    this.cities = cities
    this.getData()
  },
  mounted () {
    this.$store.commit('setActiveName', '设置')
    if (this.isLogin) {
      this.getMsg(this.userId)
    }
  },
  methods: {
    goback (index) {
      this.$router.go(index)
    },
    saveMsg () {
      this.$refs['registerForm'].validate(valid => {
        if (valid) {
          // 判断用户是否已经存在，要求严格，用户名不能重复
          let flag = this.tableData.every(item => this.registerForm.username !== item.username)
          if (flag) {
            let d = this.registerForm.birth
            let date = new Date(d)
            let dateTime = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate()
            let params = new URLSearchParams()
            params.append('id', this.userId)
            params.append('username', this.registerForm.username)
            params.append('password', this.registerForm.password)
            params.append('sex', this.registerForm.sex)
            params.append('phoneNum', this.registerForm.phoneNum)
            params.append('email', this.registerForm.email)
            params.append('birth', dateTime)
            params.append('introduction', this.registerForm.introduction)
            params.append('location', this.registerForm.location)
            updateUserMsg(params).then(res => {
              if (res.code === 1) {
                this.$store.commit('setUsername', this.registerForm.username)
                this.$notify({
                  title: '修改成功',
                  type: 'success'
                })
                setTimeout(() => {
                  this.$router.push('/')
                  this.$store.commit('setActiveName', '首页')
                }, 2000)
              } else {
                this.$message({
                  message: `修改失败, ${res.msg}`,
                  type: 'error'
                })
              }
            }).catch(err => {
              this.$message({
                message: '修改失败, 请稍后重试',
                type: 'error'
              })
              console.log(err)
            })
          } else {
            this.notify('用户名不能重复', 'warning')
            return false
          }
        } else {
          this.notify('请按要求填写表单', 'warning')
          return false
        }
      })
    },
    getMsg (userId) {
      getUserOfId(userId).then(res => {
        this.registerForm.username = res.username
        this.registerForm.password = res.password
        this.registerForm.sex = res.sex
        this.registerForm.phoneNum = res.phoneNum
        this.registerForm.email = res.email
        this.registerForm.birth = res.birth
        this.registerForm.introduction = res.introduction
        this.registerForm.location = res.location
      })
    },
    // 查询所有用户
    getData () {
      this.tableData = []
      getAllConsumer().then(res => {
        this.tableData = res
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/info.scss';
</style>
