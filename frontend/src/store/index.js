import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

// 从localStorage恢复状态
const savedToken = localStorage.getItem('token')
const savedUser = localStorage.getItem('user')

export default new Vuex.Store({
  state: {
    token: savedToken || null,
    user: savedUser ? JSON.parse(savedUser) : null
  },
  getters: {
    isLoggedIn: state => !!state.token && !!state.user,
    userId: state => state.user ? state.user.id : null,
    token: state => state.token
  },
  mutations: {
    setAuth(state, { token, user }) {
      state.token = token
      state.user = user
    },
    setUser(state, user) {
      state.user = user
    },
    logout(state) {
      state.token = null
      state.user = null
    }
  },
  actions: {
    login({ commit }, { token, user }) {
      commit('setAuth', { token, user })
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
    },
    updateUser({ commit }, user) {
      commit('setUser', user)
      localStorage.setItem('user', JSON.stringify(user))
    },
    logout({ commit }) {
      commit('logout')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
