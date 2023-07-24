import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/index'
import 'animate.css'
import './assets/css/index.scss'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/js/iconfont.js'
import '@/assets/js/iconfont1.js'
import '@/assets/js/iconfont2.js'
import '@/assets/js/iconfont3.js'

Vue.use(ElementUI)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  router,
  store, // 注入store
  components: { App },
  template: '<App/>'
}).$mount('#app')
