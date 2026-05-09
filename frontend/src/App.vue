<template>
  <div id="app">
    <el-container>
      <!-- 顶部导航 -->
      <el-header>
        <div class="header-content">
          <div class="logo" @click="$router.push('/')">
            <span class="logo-icon">🎬</span>
            <span class="logo-text">淘票票影评社区</span>
          </div>
          <div class="nav-right">
            <template v-if="user">
              <el-dropdown @command="handleCommand">
                <span class="user-info">
                  <el-avatar :src="user.avatar" :size="36"></el-avatar>
                  <span class="username">{{ user.nickname || user.username }}</span>
                  <i class="el-icon-arrow-down"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="profile" icon="el-icon-user">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout" divided icon="el-icon-switch-button">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button type="text" class="nav-btn" @click="$router.push('/login')">登录</el-button>
              <el-button class="register-btn" @click="$router.push('/register')">注册</el-button>
            </template>
          </div>
        </div>
      </el-header>
      
      <!-- 主内容区 -->
      <el-main>
        <transition name="fade" mode="out-in">
          <router-view />
        </transition>
      </el-main>
      
      <!-- 底部 -->
      <el-footer>
        <div class="footer-content">
          <div class="footer-main">
            <div class="footer-brand">
              <span class="footer-logo">🎬</span>
              <span class="footer-title">淘票票影评社区</span>
            </div>
            <p class="footer-slogan">发现好电影，分享真感受</p>
          </div>
          <p class="footer-copyright">© 2024 淘票票影评社区 All Rights Reserved</p>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import { mapState, mapMutations } from 'vuex'

export default {
  name: 'App',
  computed: {
    ...mapState(['user'])
  },
  created() {
    // 从localStorage恢复用户状态
    const savedUser = localStorage.getItem('user')
    if (savedUser) {
      this.setUser(JSON.parse(savedUser))
    }
  },
  methods: {
    ...mapMutations(['setUser', 'logout']),
    handleCommand(command) {
      if (command === 'profile') {
        this.$router.push('/profile')
      } else if (command === 'logout') {
        this.logout()
        localStorage.removeItem('user')
        this.$message.success('已退出登录')
        this.$router.push('/')
      }
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  min-height: 100vh;
  background: #f8f9fa;
}

.el-container {
  min-height: 100vh;
}

.el-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 20px rgba(102, 126, 234, 0.3);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
  transition: transform 0.3s;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  font-size: 32px;
  margin-right: 12px;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.2));
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 1px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.nav-btn {
  color: white !important;
  font-size: 15px;
  padding: 8px 16px;
  transition: all 0.3s;
}

.nav-btn:hover {
  background: rgba(255,255,255,0.1);
  border-radius: 20px;
}

.register-btn {
  background: rgba(255,255,255,0.2) !important;
  border: 2px solid rgba(255,255,255,0.5) !important;
  color: white !important;
  border-radius: 20px !important;
  padding: 8px 20px !important;
  font-weight: 500;
  transition: all 0.3s !important;
}

.register-btn:hover {
  background: white !important;
  color: #667eea !important;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: white;
  padding: 6px 12px;
  border-radius: 25px;
  transition: all 0.3s;
  background: rgba(255,255,255,0.1);
}

.user-info:hover {
  background: rgba(255,255,255,0.2);
}

.username {
  margin-left: 10px;
  font-size: 14px;
  font-weight: 500;
}

.user-info .el-icon-arrow-down {
  margin-left: 6px;
  font-size: 12px;
}

.el-main {
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 30px 20px;
  min-height: calc(100vh - 140px);
}

#app .el-footer {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  color: #a0a0a0;
  text-align: center;
  padding: 40px 20px;
  height: auto !important;
  min-height: 120px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-main {
  margin-bottom: 20px;
}

.footer-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 8px;
}

.footer-logo {
  font-size: 28px;
}

.footer-title {
  font-size: 20px;
  font-weight: 600;
  color: white;
}

.footer-slogan {
  color: #888;
  font-size: 14px;
  margin: 0;
}

.footer-links {
  margin: 20px 0;
}

.footer-links a {
  color: #888;
  text-decoration: none;
  font-size: 13px;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: #667eea;
}

.footer-links .divider {
  margin: 0 15px;
  color: #444;
}

.footer-copyright {
  font-size: 12px;
  color: #555;
  margin: 0;
}

/* 页面切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

/* 全局美化 */
.el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border: none !important;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  transition: all 0.3s !important;
}

.el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.el-input__inner:focus {
  border-color: #667eea !important;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.el-rate__icon {
  font-size: 18px;
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #764ba2;
}
</style>
