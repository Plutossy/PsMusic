import Axios from 'axios'
import { get, post } from './http'

// =================歌手相关=======================
// 查询歌手
export const getAllSinger = () => get(`singer/allSinger`)

// 根据歌手性别查询歌手列表
export const getSingerOfSex = (sex) => get(`singer/singerOfSex?sex=${sex}`)

// ================歌曲相关========================
// 根据歌手id查询歌曲
export const songOfSingerId = (id) => get(`song/singer/detail?singerId=${id}`)

// 根据歌曲id查询歌曲对象
export const songOfSongId = (id) => get(`song/detail?songId=${id}`)

// 根据歌曲名获取歌曲对象
export const likeSongOfName = (songName) => get(`song/likeSongOfName?songName=${songName}`)

// // 查询所有歌曲
// export const getAllSong = () => get(`song/allSong`)

// =================歌单相关=======================
// 查询歌单
export const getAllSongList = () => get(`songList/allSongList`)

// 返回标题包含文字的歌单列表
export const getSongListLikeTitle = (keywords) => get(`songList/likeTitle?title=${keywords}`)

// 根据歌单风格查询对应歌单
export const getSongListOfStyle = (style) => get(`songList/likeStyle?style=${style}`)

// =================歌单歌曲相关=======================
// 根据歌单id查询歌曲
export const listSongDetail = (id) => get(`listSong/detail?songListId=${id}`)

// =================用户相关=======================
// 查询用户
export const getAllConsumer = () => get(`consumer/allConsumer`)

// 注册
export const signUp = (params) => post(`consumer/add`, params)

// 登录
export const login = (params) => post(`consumer/login`, params)

// 更新用户信息
export const updateUserMsg = (params) => post(`consumer/update`, params)

// 根据id查询用户详情信息
export const getUserOfId = (id) => get(`consumer/selectByPrimaryKey?id=${id}`)

// =================下载音乐=======================
// 下载音乐
export const download = (url) => Axios({
  method: 'get',
  url: url,
  responseType: 'blob'
})

// =================评分=======================
// 新增评分
export const setRank = (params) => post(`/rank/add`, params)

// 获取平均分
export const getRankOfSongListId = (songListId) => get(`/rank?songListId=${songListId}`)

// =================评论=======================
// 新增评论
export const setComment = (params) => post(`/comment/add`, params)

// 给评论点赞
export const setLike = (params) => post(`/comment/like`, params)

// 根据歌单id查询评论
export const getAllComment = (type, id) => {
  if (type === 0) { // 歌曲评论
    return get(`/comment/commentOfSongId?songId=${id}`)
  } else { // 歌单评论
    return get(`/comment/commentOfSongListId?songListId=${id}`)
  }
}

// 根据评论id查询这条评论
export const getCommentOfId = (id) => get(`/comment/selectByPrimaryKey?id=${id}`)

// =================评论点赞=======================
// 新增评论点赞
export const addCommentUp = (params) => post(`/commentUp/add`, params)

// 删除评论点赞
export const deleteCommentUp = (userId, commentId) => get(`/commentUp/delete?userId=${userId}&commentId=${commentId}`)

// 根据主键查询评论点赞
export const getCommentUp = (id) => get(`/commentUp/selectByPrimaryKey?id=${id}`)

// =================收藏=======================
// 新增收藏
export const setCollect = (params) => post(`/collect/add`, params)

// 指定用户的收藏列表
export const getCollectOfUserId = (userId) => get(`/collect/collectOfUserId?userId=${userId}`)
