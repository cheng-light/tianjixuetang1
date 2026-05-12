import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import Courses from '../views/Courses.vue'
import CourseDetail from '../views/CourseDetail.vue'
import Homeworks from '../views/Homeworks.vue'
import Exams from '../views/Exams.vue'
import Admin from '../views/Admin.vue'
import Profile from '../views/Profile.vue'

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/login', component: Login },
  { path: '/dashboard', component: Dashboard, meta: { auth: true } },
  { path: '/courses', component: Courses, meta: { auth: true } },
  { path: '/courses/:id', component: CourseDetail, meta: { auth: true } },
  { path: '/homeworks', component: Homeworks, meta: { auth: true } },
  { path: '/exams', component: Exams, meta: { auth: true } },
  { path: '/profile', component: Profile, meta: { auth: true } },
  { path: '/admin', component: Admin, meta: { auth: true, role: 'ADMIN' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.auth && !auth.isLoggedIn) {
    window.location.href = '/auth.html'
    return false
  }
  if (to.meta.role && !auth.roles.includes(to.meta.role)) {
    return '/dashboard'
  }
})

export default router
