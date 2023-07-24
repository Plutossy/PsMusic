<template>
  <div class="table">
    <div class="crumbs">
      <i class="el-icon-tickets">&nbsp;歌单歌曲信息</i>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="danger" @click="delAll" size="mini">批量删除</el-button>
        <el-input v-model="select_word" size="mini" placeholder="请输入歌曲名" class="handle-input"></el-input>
        <el-button type="primary" @click="centerDialogVisible = true" size="mini">添加歌曲</el-button>
      </div>
    </div>
    <el-table size="mini" ref="multipleTable" border style="width: 100%" height="680px" :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="40"></el-table-column>
      <el-table-column prop="name" label="歌手-歌名" align="center"></el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 添加歌曲弹出框 -->
    <el-dialog title="添加歌曲" :visible.sync="centerDialogVisible" width="400px" center>
      <el-form :model="registerForm" ref="registerForm" lobel-width="80px" action="" id="tf">
        <el-form-item prop="singerName" label="歌手名字" size="mini">
          <el-input v-model="registerForm.singerName" placeholder="歌手名字"></el-input>
        </el-form-item>
        <el-form-item prop="songName" label="歌曲名字" size="mini">
          <el-input v-model="registerForm.songName" placeholder="歌曲名字"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="mini" @click="centerDialogVisible= false">取消</el-button>
        <el-button size="mini" @click="getSongId">确定</el-button>
      </span>
    </el-dialog>
    <!-- 删除歌曲弹出框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
      <div align="center">此操作将永久删除该歌曲, 是否继续?</div>
      <span slot="footer">
        <el-button size="mini" @click="delVisible= false">取消</el-button>
        <el-button size="mini" @click="deleteRow">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mixin } from '@/mixins/index'
import { listSongDetail, songOfSongId, songOfSongName, listSongAdd, delListSong } from '@/api/index'

export default {
  mixins: [ mixin ],
  data () {
    return {
      centerDialogVisible: false, // 添加对话框
      delVisible: false, // 删除对话框
      registerForm: { // 添加表单
        singerName: '', // 歌手名
        songName: '' // 歌名
      },
      tableData: [],
      tempData: [],
      select_word: '',
      idx: -1, // 当前选择项
      multipleSelection: [], // 当前选择项
      songListId: '', // 歌单id
      songAndName: '' // 拼接歌手名和歌名
    }
  },
  created () {
    this.songListId = this.$route.query.id
    this.getData()
  },
  watch: {
    // 监听搜索框的值
    select_word (val) {
      if (this.select_word === '') {
        this.tableData = this.tempData
      } else {
        this.tableData = []
        // 从所有歌手中筛选出包含搜索内容的歌手
        this.tableData = this.tempData.filter(item => {
          return item.name.indexOf(val) !== -1 // 返回包含搜索内容的歌手
        })
      }
    }
  },
  methods: {
    // 查询所有歌曲
    getData () {
      let _this = this
      this.tableData = []
      this.tempData = []
      listSongDetail(this.songListId).then(res => {
        res.forEach(item => {
          _this.getSong(item.songId)
        })
      }).catch(err => {
        console.log(err)
      })
    },
    // 根据歌曲id查询歌曲对象，放到 tempData 和 tableData中
    getSong (id) {
      songOfSongId(id).then(res => {
        this.tempData.push(res)
        this.tableData.push(res)
      }).catch(err => {
        console.log(err)
      })
    },
    // 添加歌曲前的准备，获取到歌曲id
    getSongId () {
      // 拼接歌手名和歌名
      this.songAndName = this.registerForm.singerName + '-' + this.registerForm.songName
      songOfSongName(this.songAndName).then(res => {
        this.addSong(res[0].id)
      }).catch(err => {
        // this.$message.error('不能添加歌单以外的歌曲')
        this.notify('不能添加歌单以外的歌曲', 'warning')
        console.log(err)
      })
    },
    // 添加歌曲
    addSong (songId) {
      // 判断歌曲是否已经存在
      let flag = this.tableData.every(item => item.name !== this.songAndName)
      if (flag) {
        let params = new URLSearchParams()
        params.append('songId', songId)
        params.append('songListId', this.songListId)
        // 发送数据请求到后台
        listSongAdd(params).then(res => {
          if (res.code === 1) {
          // 重新获取数据
            this.getData()
            // 清空表单
            this.registerForm = {
              singerName: '', // 歌手名
              songName: '' // 歌名
            }
            this.notify('添加成功', 'success')
          } else {
            this.notify('添加失败', 'error')
          }
        }).catch(err => {
          console.log(err)
        })
      } else {
        // 清空表单
        this.registerForm = {
          singerName: '', // 歌手名
          songName: '' // 歌名
        }
        this.notify('歌曲已存在, 请勿重复添加', 'warning')
      }
      this.centerDialogVisible = false
    },
    // 确定删除当前行歌曲信息
    deleteRow () {
      delListSong(this.idx, this.songListId).then(res => {
        if (res) {
          // 重新获取数据
          this.getData()
          this.notify('删除成功', 'success')
        } else {
          this.notify('删除失败', 'error')
        }
      }).catch(err => {
        console.log(err)
      })
      this.delVisible = false
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
.crumbs {
  margin: 20px 10px;
  font-size: 20px;
  font-weight: bold;
}
.song-img {
  width: 100%;
  height: 80px;
  border-radius: 5px;
  margin-bottom: 5px;
  overflow: hidden;
}
.handle-input {
  width: 200px;
  margin: 0 20px;
  display: inline-block;
}
.pagination {
  display: flex;
  justify-content: center;
}
.musicInput {
  width: 340px;
  height: 30px;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 5px;
  margin-top: 5px;
}
.paly {
  position: absolute;
  z-index: 999;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  top: 20px;
  left: 18px;
}
.icon {
  width: 2em;
  height: 2em;
  color: rgb(96, 235, 225);
  fill: currentColor;
  overflow: hidden;
}
</style>
