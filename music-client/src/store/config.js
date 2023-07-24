const config = ({
  state: {
    HOST: 'http://localhost:8888', // 服务器地址
    activeName: '', // 当前激活的导航菜单名称
    showAside: false, // 是否显示歌曲播放列表
    isLogin: false, // 是否登录
    isActive: false // 当前歌曲是否已收藏
  },
  getters: {
    activeName: state => { // 获取当前激活的导航菜单名称
      let activeName = state.activeName
      if (!activeName) {
        activeName = JSON.parse(window.sessionStorage.getItem('activeName'))
      }
      return activeName
    },
    showAside: state => { // 获取是否显示歌曲播放列表
      let showAside = state.showAside
      if (!showAside) {
        showAside = JSON.parse(window.sessionStorage.getItem('showAside'))
      }
      return showAside
    },
    isLogin: state => { // 获取是否登录
      let isLogin = state.isLogin
      if (!isLogin) {
        isLogin = JSON.parse(window.sessionStorage.getItem('isLogin'))
      }
      return isLogin
    },
    isActive: state => { // 获取当前歌曲是否已收藏
      let isActive = state.isActive
      if (!isActive) {
        isActive = JSON.parse(window.sessionStorage.getItem('isActive'))
      }
      return isActive
    }
  },
  mutations: {
    setActiveName (state, activeName) { // 设置当前激活的导航菜单名称
      state.activeName = activeName
      window.sessionStorage.setItem('activeName', JSON.stringify(activeName))
    },
    setShowAside (state, showAside) { // 设置是否显示歌曲播放列表
      state.showAside = showAside
      window.sessionStorage.setItem('showAside', JSON.stringify(showAside))
    },
    setIsLogin (state, isLogin) { // 设置是否登录
      state.isLogin = isLogin
      window.sessionStorage.setItem('isLogin', JSON.stringify(isLogin))
    },
    setIsActive (state, isActive) { // 设置当前歌曲是否已收藏
      state.isActive = isActive
      window.sessionStorage.setItem('isActive', JSON.stringify(isActive))
    }
  }
})

export default config
