import Vue from 'vue'
import Vuex from 'vuex'
import config from './config'
import user from './user'
import song from './song'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: { // modules表示模块化输出
    config,
    user,
    song
  }
})

export default store
