import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/pages/Home'
import MyMusic from '@/pages/MyMusic'
import Singer from '@/pages/Singer.vue'
import SongList from '@/pages/SongList'
import Search from '@/pages/Search'
import Lyric from '@/pages/Lyric'
import Login from '@/pages/Login'
import SignUp from '@/pages/SignUp'
import Setting from '@/pages/Setting'
import SingerAlbum from '@/pages/SingerAlbum'
import SongListAlbum from '@/pages/SongListAlbum.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/my-music', name: 'my-music', component: MyMusic },
    { path: '/singer', name: 'singer', component: Singer },
    { path: '/song-list', name: 'song-list', component: SongList },
    { path: '/search', name: 'search', component: Search },
    { path: '/lyric', name: 'lyric', component: Lyric },
    { path: '/login', name: 'login', component: Login },
    { path: '/sign-up', name: 'sign-up', component: SignUp },
    { path: '/setting', name: 'setting', component: Setting },
    { path: '/singer-album/:id', name: 'singer-album', component: SingerAlbum },
    { path: '/song-list-album/:id', name: 'song-list-album', component: SongListAlbum }
  ],
  scrollBehavior (to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
})
