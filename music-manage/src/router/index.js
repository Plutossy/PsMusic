import Vue from 'vue'
import Router from 'vue-router'
import Arrrouter from './router'

Vue.use(Router)

const router = new Router({
  mode: 'history', // 去掉url中的#
  routes: [
    {
      path: '/',
      component: resolve => require(['../pages/Login.vue'], resolve)
    },
    {
      path: '/home',
      component: resolve => require(['../components/Home.vue'], resolve),
      children: [
        {
          path: '/info',
          component: resolve => require(['../pages/InfoPage.vue'], resolve)
        },
        {
          path: '/consumer',
          component: resolve => require(['../pages/ConsumerPage.vue'], resolve)
        },
        {
          path: '/singer',
          component: resolve => require(['../pages/SingerPage.vue'], resolve)
        },
        {
          path: '/songlist',
          component: resolve => require(['../pages/SongListPage.vue'], resolve)
        },
        {
          path: '/song',
          component: resolve => require(['../pages/SongPage.vue'], resolve)
        },
        {
          path: '/listsong',
          component: resolve => require(['../pages/ListSongPage.vue'], resolve)
        },
        {
          path: '/collect',
          component: resolve => require(['../pages/CollectPage.vue'], resolve)
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  if (Arrrouter.indexOf(to.path) !== -1) {
    const token = localStorage.getItem('token')
    const username = sessionStorage.getItem('userName')
    if (token === 'Bearer ' + username + '-1') {
      // 有 token，放行
      next()
    } else {
      // 没有 token，强制跳转到 登录界面
      next('/')
    }
  } else {
    // 不是 /main，直接放行
    next()
  }
})

export default router
