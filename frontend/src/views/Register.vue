<template>
  <div class="register-page">
    <div class="register-card">
      <h2 class="title">用户注册</h2>
      <el-form :model="form" :rules="rules" ref="form" label-width="0">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            prefix-icon="el-icon-user"
            placeholder="请输入用户名"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password"
            prefix-icon="el-icon-lock"
            placeholder="请输入密码"
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="form.confirmPassword" 
            type="password"
            prefix-icon="el-icon-lock"
            placeholder="请确认密码"
          />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input 
            v-model="form.nickname" 
            prefix-icon="el-icon-s-custom"
            placeholder="请输入昵称（选填）"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading"
            style="width: 100%"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="footer">
        已有账号？<router-link to="/login">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { userApi } from '@/api'
import { mapActions } from 'vuex'

export default {
  name: 'Register',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      form: {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '用户名长度在3-20个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在6-20个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    ...mapActions(['login']),
    handleRegister() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        this.loading = true
        try {
          const res = await userApi.register({
            username: this.form.username,
            password: this.form.password,
            nickname: this.form.nickname || this.form.username
          })
          // 保存token和用户信息
          this.login({ token: res.data.token, user: res.data.user })
          this.$message.success('注册成功')
          this.$router.push('/')
        } catch (e) {
          this.$message.error(e.message || '注册失败')
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 180px);
  padding: 20px;
}

.register-card {
  background: white;
  padding: 50px 40px;
  border-radius: 20px;
  width: 420px;
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.15);
  position: relative;
  overflow: hidden;
}

.register-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.title {
  text-align: center;
  margin-bottom: 35px;
  color: #2d3436;
  font-size: 26px;
  font-weight: 700;
}

.register-card >>> .el-input__inner {
  height: 48px;
  border-radius: 12px;
  font-size: 15px;
  border: 2px solid #e9ecef;
  padding-left: 45px;
}

.register-card >>> .el-input__inner:focus {
  border-color: #667eea;
}

.register-card >>> .el-input__prefix {
  left: 15px;
  font-size: 18px;
  color: #b2bec3;
}

.register-card >>> .el-form-item {
  margin-bottom: 22px;
}

.register-card >>> .el-button {
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
}

.footer {
  text-align: center;
  color: #636e72;
  font-size: 14px;
  margin-top: 25px;
}

.footer a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s;
}

.footer a:hover {
  color: #764ba2;
}
</style>
