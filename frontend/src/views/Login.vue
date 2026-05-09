<template>
  <div class="login-page">
    <div class="login-card">
      <h2 class="title">用户登录</h2>
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
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            :loading="loading"
            style="width: 100%"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="footer">
        还没有账号？<router-link to="/register">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { userApi } from '@/api'
import { mapActions } from 'vuex'

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    ...mapActions(['login']),
    handleLogin() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        this.loading = true
        try {
          const res = await userApi.login(this.form)
          // 保存token和用户信息
          this.login({ token: res.data.token, user: res.data.user })
          this.$message.success('登录成功')
          this.$router.push('/')
        } catch (e) {
          this.$message.error(e.message || '登录失败')
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 180px);
  padding: 20px;
}

.login-card {
  background: white;
  padding: 50px 40px;
  border-radius: 20px;
  width: 420px;
  box-shadow: 0 20px 60px rgba(102, 126, 234, 0.15);
  position: relative;
  overflow: hidden;
}

.login-card::before {
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

.login-card >>> .el-input__inner {
  height: 48px;
  border-radius: 12px;
  font-size: 15px;
  border: 2px solid #e9ecef;
  padding-left: 45px;
}

.login-card >>> .el-input__inner:focus {
  border-color: #667eea;
}

.login-card >>> .el-input__prefix {
  left: 15px;
  font-size: 18px;
  color: #b2bec3;
}

.login-card >>> .el-form-item {
  margin-bottom: 25px;
}

.login-card >>> .el-button {
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
