import { defineStore } from 'pinia'
import { authApi } from '../api/modules'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token'),
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    roles: JSON.parse(localStorage.getItem('roles') || '[]')
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.token)
  },
  actions: {
    async login(form) {
      const data = await authApi.login(form)
      this.token = data.token
      this.user = {
        userId: data.userId,
        username: data.username,
        nickname: data.nickname
      }
      this.roles = data.roles
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(this.user))
      localStorage.setItem('roles', JSON.stringify(this.roles))
    },
    logout() {
      this.token = null
      this.user = null
      this.roles = []
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('roles')
    }
  }
})
