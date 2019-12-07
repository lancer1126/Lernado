<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="提交审核"/>
    </el-steps>

    <el-button type="text" @click="openAddChapter()">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
        <li
            v-for="chapter in chapterVideoList"
            :key="chapter.id">
            <p>
                {{ chapter.title }}
              <span class="acts">
                  <el-button type="text" @click="openVideoDialog(chapter.id)">添加小节</el-button>
                  <el-button style="" type="text" @click="getChapterById(chapter.id)">编辑</el-button>
                  <el-button type="text" @click="removeChapter(chapter.id)">删除</el-button>
              </span>
            </p>

            <!-- 小节 -->
            <ul class="chanpterList videoList">
                <li
                    v-for="video in chapter.children"
                    :key="video.id">
                    <p>{{ video.title }}
                        <span class="acts">
                          <el-button type="text" @click="openVideoEditDialog(video.id)">编辑</el-button>
                          <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
                        </span>
                    </p>
                </li>
            </ul>
        </li>
    </ul>

    <div>
        <el-button @click="previous">上一步</el-button>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
        <el-form :model="chapter" label-width="120px">
            <el-form-item label="章节标题">
                <el-input v-model="chapter.title"/>
            </el-form-item>
            <el-form-item label="章节排序">
                <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
        </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogVidoFormVisible" title="添加小节">
        <el-form :model="video" label-width="120px">
            <el-form-item label="小节标题">
                <el-input v-model="video.title"/>
            </el-form-item>
            <el-form-item label="小节排序">
                <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogVidoFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdateVideo">确 定</el-button>
        </div>
    </el-dialog>

  </div>
</template>

<script>
import chapter from '@/api/chapter'
import video from '@/api/video'

export default {
    data() {
        return {
            id: '',
            chapterVideoList: [], // 章节嵌套视频列表
            // 章节对象
            chapter: {
                title: '',
                sort: 0,
                courseId:''
            },
            // 小节对象
            video: {
                title: '',
                sort:'',
                chapterId: '',
                courseId: ''
            },
            dialogChapterFormVisible: false,
            dialogVidoFormVisible: false
        }
    },
    created() {
        this.init()
    },
    methods: {
        init() {
            // 若路由中有id值则将id传给本页面中的id
            if(this.$route.params && this.$route.params.id) {
                this.id = this.$route.params.id
                this.getChapterVideoId(this.id)
            }
        },
        previous() {
            // 使用previous方法转到具有传递过来的id值的界面,即上一个界面
            this.$router.push({path:'/course/info/'+this.id})
        },
        next() {
            this.$router.push({path:'/course/publish/'+this.id})
        },
        //根据课程id查询章节和小节信息
        getChapterVideoId(id) {
          chapter.getAllChapterVideoList(id)
            .then(response => {
                //赋值给集合
                this.chapterVideoList = response.data.items
            })
            .catch(response => {
              this.$message({
                    type: 'error',
                    message: '数据加载失败!'
                })
            })
        },
        saveOrUpdate() {
            if(!this.chapter.id) {
                this.saveChapter()
            } else {
                this.updateChapter()
            }
            
        },
        saveChapter() {
            // 设置课程id
            this.chapter.courseId = this.id
            chapter.addChapter(this.chapter)
            .then(response => {
                this.dialogChapterFormVisible = false
                this.$message({
                    type: 'success',
                    message: '添加成功'
                })
                // 重新刷新界面
                this.getChapterVideoId(this.id)
            }).catch(response => {
                this.$message({
                    type: 'error',
                    message: '添加失败'
                })
            })
        },
        updateChapter() {
            chapter.updateChapter(this.chapter)
            .then(response => {
                  //关闭弹框
                  this.dialogChapterFormVisible = false
                  this.$message({
                    type: 'success',
                    message: '修改章节成功!'
                  })
                  //刷新页面
                  this.getChapterVideoId(this.id)
            })
            .catch(response => {
                  this.$message({
                    type: 'error',
                    message: '修改章节失败!'
                  })
            })
        },
        // 点击编辑按钮时进行修改操作
        getChapterById(chapterid) {
            this.dialogChapterFormVisible = true
            chapter.getChapterInfo(chapterid)
            .then(response => {
                this.chapter = response.data.eduChapter
            })
        },
       //删除章节的方法
        removeChapter(id) {
            this.$confirm('此操作将永久删除章节,是否继续?', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                return chapter.deleteChapter(id)
            }).then(() => {
                this.$message({
                    type: 'success',
                    message: '删除成功'
                })
            }).catch(() =>{
                if(response === 'cancel') {
                    this.message({
                        type: 'success',
                        message: '已取消删除'
                    })
                } else {
                    this.$message({
                        type: 'error',
                        message: '删除失败'
                    })
                }
            })
        },
         //在点击添加时不数据回显
        openAddChapter() {
            this.dialogChapterFormVisible = true
            // 将表单清空
            this.chapter.title = ''
            this.chapter.sort = ''
            this.chapter.courseId = ''
        },
        
        // 对小节的各种操作
        //点击编辑按钮，弹出框，显示小节数据
        openVideoEditDialog(videoId) {
            this.dialogVidoFormVisible = true
            video.getVideoInfo(videoId)
            .then(response => {
                this.video = response.data.eduVideo
            })
        },
         //点击添加小节，弹出框，需要章节id
        openVideoDialog(chapterid) {
            this.dialogVidoFormVisible = true
            this.video.title = ''
            this.video.sort = ''
            this.video.chapterId = chapterid
        },
        saveOrUpdateVideo() {
            if(!this.video.id) {
                this.addVideo()
            } else {
                this.updateVideo()
            }
        },
        // 修改小节
        updateVideo() {
            video.updateVideo(this.video)
            .then(response => {
                //关闭弹框
                this.dialogVidoFormVisible = false
                this.$message({
                    type: 'success',
                    message: '修改小节成功!'
                })
                //刷新页面
                this.getChapterVideoId(this.id)
            })
            .catch(response => {
                this.$message({
                type: 'error',
                message: '修改小节失败!'
                })
            })
        },
        addVideo() {
            this.video.courseId = this.id
            video.addVideo(this.video)
            .then(() => {
                this.dialogVidoFormVisible = false
                //提示信息
                this.$message({
                  type: 'success',
                  message: '添加小节成功!'
                })
                this.getChapterVideoId(this.id)
            }).catch(() => {
                this.$message({
                    type: 'error',
                    message: '添加小节失败!'
                })
            })
        },
        //删除小节
        removeVideo(id) {
            this.$confirm('此操作将永久删除该小节, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //调用方法进行删除
                //return 表示后面then还会执行
                return video.deleteVideo(id)
            }).then(() => {
                //刷新整个页面
                this.getChapterVideoId(this.id)
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                })
            }).catch(() => {
                //判断点击取消，还是删除失败
                if (response === 'cancel') {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    })
                } else {
                    this.$message({
                        type: 'error',
                        message: '删除失败'
                    })
                }        
            })
        }
    }
}
   
</script>
