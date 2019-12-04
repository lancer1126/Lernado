<template>

  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="提交审核"/>
    </el-steps>

    <el-button type="text" @click="dialogChapterFormVisible = true">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
        <li
            v-for="chapter in chapterVideoList"
            :key="chapter.id">
            <p>
                {{ chapter.title }}
              <span class="acts">
                  <el-button type="text">添加小节</el-button>
                  <el-button style="" type="text" @click="getChapterById(chapter.id)">编辑</el-button>
                  <el-button type="text">删除</el-button>
              </span>
            </p>

            <!-- 小节 -->
            <ul class="chanpterList videoList">
                <li
                    v-for="video in chapter.children"
                    :key="video.id">
                    <p>{{ video.title }}
                        <span class="acts">
                          <el-button type="text">编辑</el-button>
                          <el-button type="text">删除</el-button>
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

  </div>
</template>

<script>
import chapter from '@/api/chapter'

export default {
    data() {
        return {
            id: '',
            chapterVideoList: [], // 章节嵌套视频列表
            chapter: {
                title: '',
                sort: 0,
                courseId:''
          },
            dialogChapterFormVisible:false
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
            this.$router.push({path:'/course/publish/1'})
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
            if(!chapter.id) {
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
                    type: 'success',
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
    }
    },
}
</script>

<style scoped>
    .chanpterList{
        position: relative;
        list-style: none;
        margin: 0;
        padding: 0;
    }
    .chanpterList li{
        position: relative;
    }
    .chanpterList p{
        float: left;
        font-size: 20px;
        margin: 10px 0;
        padding: 10px;
        height: 70px;
        line-height: 50px;
        width: 100%;
        border: 1px solid #DDD;
    }
    .chanpterList .acts {
        float: right;
        font-size: 14px;
    }

    .videoList{
        padding-left: 50px;
    }
    .videoList p{
        float: left;
        font-size: 14px;
        margin: 10px 0;
        padding: 10px;
        height: 50px;
        line-height: 30px;
        width: 100%;
        border: 1px dotted #DDD;
    }
</style>