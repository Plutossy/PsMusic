<template>
  <div>
    <login-logo />
    <div class="login animate__animated animate__fadeInDown">
      <div class="login-head">
        <span>账号登录</span>
      </div>
      <el-form :model="loginForm" ref="loginForm" label-width="70px" class="demo-ruleForm" :rules="rules">
        <el-form-item prop="username" label="用户名">
          <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input type="password" v-model="loginForm.password" placeholder="密码"></el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button @click="goSignUp">注册</el-button>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mixin } from '@/mixins/index.js'
import LoginLogo from '@/components/LoginLogo.vue'
import { login } from '@/api/index.js'

export default {
  mixins: [mixin],
  name: 'login',
  components: {
    LoginLogo
  },
  data () {
    return {
      loginForm: {
        username: '', // 用户名
        password: '' // 密码
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.$store.commit('setActiveName', '登录')
  },
  methods: {
    goSignUp () {
      this.$router.push('/sign-up')
      this.$store.commit('setActiveName', '注册')
    },
    handleLogin () {
      let params = new URLSearchParams()
      params.append('username', this.loginForm.username)
      params.append('password', this.loginForm.password)
      login(params).then(res => {
        if (res.code === 1) {
          this.$notify({
            title: '登录成功',
            type: 'success'
          })
          this.$store.commit('setIsLogin', true)
          this.$store.commit('setUserId', res.userMsg.id)
          this.$store.commit('setUsername', res.userMsg.username)
          this.$store.commit('setAvatar', res.userMsg.avatar)
          setTimeout(() => {
            this.$router.push('/')
            this.$store.commit('setActiveName', '首页')
          }, 500)
        } else {
          this.$message({
            message: `登录失败, ${res.msg}`,
            type: 'error'
          })
        }
      }).catch(err => {
        this.$message({
          message: '登录失败, 请稍后重试',
          type: 'error'
        })
        console.log(err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../assets/css/login-in.scss';
</style>
