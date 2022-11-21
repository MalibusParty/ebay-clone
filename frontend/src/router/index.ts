import { useLogin } from '@/services/useLogin'
import { createRouter, createWebHistory } from 'vue-router'
import AngeboteView from '../views/AngeboteView.vue'
import GebotView from '../views/GebotView.vue'
import LoginView from '../views/LoginView.vue'

const { logindata } = useLogin();

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'angebote',
      component: AngeboteView
    },
    {
      path: '/gebot/:angebotidstr',
      name: 'gebot',
      component: GebotView,
      props: true
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    }
  ]
})

router.beforeEach(async(to, from) => {
  // wenn z.B. ein 'berechtigt' nicht wahr ist,
  // alle Nicht-/login-Navigationen auf /login leiten
  if (!logindata.loggedin && to.name !== 'login') {
    return { name: 'login' }
  }
})

export default router
