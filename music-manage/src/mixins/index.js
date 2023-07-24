export const mixin = {
  methods: {
    // 提示信息
    notify (title, type) {
      this.$notify({
        title: title,
        type: type
      })
    },
    // 根据相对地址获取绝对地址
    getUrl (url) {
      return `${this.$store.state.HOST}${url}`
    },
    // 获取性别中文
    changeSex (value) {
      if (value === 0) {
        return '女'
      } else if (value === 1) {
        return '男'
      } else if (value === 2) {
        return '组合'
      } else {
        return '不明'
      }
    },
    // 获取生日
    attachBirth (val) {
      return String(val).substr(0, 10)
    },
    // 上传图片之前的校验
    beforeAvatarUpload (file) {
      const isJPG = (file.type === 'image/jpeg') || (file.type === 'image/png') || (file.type === 'image/gif')
      const isLt2M = (file.size / 1024 / 1024) < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 或者 png 格式')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB')
        return false
      }
      return true
    },
    // 上传图片之后要做的事情
    handleAvatarSuccess (res, file) {
      let _this = this
      if (res.code === 1) {
        // _this.imageUrl = URL.createObjectURL(file.raw)
        _this.getData() // 重新获取数据
        _this.notify('上传成功', 'success')
      } else {
        _this.notify('上传失败', 'error')
      }
    },
    // 弹出删除窗口
    handleDelete (id) {
      this.delVisible = true
      this.idx = id
    },
    // 把已经选择的项赋值给multipleSelection
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    // 批量删除已经选择的项
    delAll () {
      if (this.multipleSelection.length === 0) {
        this.notify('请至少选择一项', 'warning')
        return
      }
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        for (let item of this.multipleSelection) {
          this.handleDelete(item.id)
          this.deleteRow()
        }
        this.multipleSelection = []
      })
    }
  }
}
