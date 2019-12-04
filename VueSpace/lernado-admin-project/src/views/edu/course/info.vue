<template>
  <div class="app-container">

    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布课程"/>
    </el-steps>

    <el-form label-width="120px">
        <el-form-item label="课程标题">
            <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"/>
        </el-form-item>

        <!-- 所属分类 TODO -->
        <!-- 一级分类 -->
        <el-form-item label="课程类别">
            <el-select
                v-model="courseInfo.subjectParentId"
                placeholder="请选择" @change="subjectLevelOneChanged">
                <el-option
                v-for="subject in oneLevelSubjectList"
                :key="subject.id"
                :label="subject.title"
                :value="subject.id"/>
            </el-select>
            <!-- 二级分类 -->
            <el-select v-model="courseInfo.subjectId" placeholder="请选择">
            <el-option
                v-for="subject in twoLevelSubjectList"
                :key="subject.value"
                :label="subject.title"
                :value="subject.id"/>
            </el-select>
        </el-form-item>

        <!-- 课程讲师 TODO -->
        <!-- 课程讲师 -->
        <el-form-item label="课程讲师">
            <el-select
                v-model="courseInfo.teacherId"
                placeholder="请选择">
                <el-option
                v-for="teacher in teacherList"
                :key="teacher.id"
                :label="teacher.name"
                :value="teacher.id"/>
            </el-select>
        </el-form-item>

        <el-form-item label="总课时">
            <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
        </el-form-item>

        <!-- 课程简介 TODO -->
        <el-form-item label="课程简介">
            <el-input v-model="courseInfo.description" placeholder=" 示例：简介"/>
        </el-form-item>

        <!-- 课程封面 TODO -->
        <el-form-item label="课程封面">
        <el-upload
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            :action="BASE_API+'/eduservice/oss/upload?host=cover'"
            class="avatar-uploader">
            <img :src="courseInfo.cover">
        </el-upload>
        </el-form-item>

        <el-form-item label="课程价格">
            <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
        </el-form-item>

        <el-form-item>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="next">保存并下一步</el-button>
        </el-form-item>
    </el-form>
  </div>
</template>

<script>
import course from '@/api/course'
import subject from '@/api/subject'


const defaultForm = {
    title: '',
    subjectId: '',
    teacherId: '',
    lessonNum: 0,
    description: '',
    cover: '/static/01.jpg',
    price: 0
}

export default {
    data() {
        return {
            saveBtnDisabled: false,
            teacherList: [],    //储存所有讲师的集合
            oneLevelSubjectList:[],//一级分类集合
            twoLevelSubjectList:[],//二级分类集合
            courseInfo: defaultForm,
            BASE_API: process.env.BASE_API // 接口API地址
        }
    }, 
    watch: {
        // 监听路由切换,若是没有此方法则下列方法都会无效
        $route(to, from) {
        console.log('watch $route')
        this.init()
        }
    },
    created() {
        this.init()

    },
    methods: {
        init() {
            // 若当前路由中存在id值则说明是由下一步返回而来,需要数据回显
            if(this.$route.params && this.$route.params.id) {
                // 数据回显
                const id = this.$route.params.id
                this.getIdCourse(id)
            } else { // 若路由中没有id值则将表单清空
                this.courseInfo = {...defaultForm}
                //页面一加载就获取所有的讲师到下拉列表中
                this.getTeacherList()
                this.getLevelAll()
            }
        },

        next() {    
            // 判断是添加还是修改操作,若是添加则是普通界面,若是修改则保存之间已经填写的数据,以路由中是否有id值为区分
            if(!this.courseInfo.id) {
                // 若没有id值则是添加界面
                this.addCourse()
            } else {
                this.updateCourse()
            }
        },

        // 获取所有讲师
        getTeacherList() {
            course.getAllTeacherList()
            .then(response =>{
                this.teacherList = response.data.items
            })
        },

        // 添加讲师信息的方法,调用方法讲保存的信息传输到后端中,并由后端保存到数据库中
        addCourse() {
            course.addCourseInfo(this.courseInfo)
            .then(response => {
                this.$message({
                    type: 'success',
                    message: '添加课程信息成功!'
                }),
                // 添加信息成功,路由跳转到页面编写大纲中
                this.$router.push({path:'/course/chapter/'+response.data.courseId})
            })
            // 信息添加失败
            .catch(() => {
                this.$message({
                    type: 'error',
                    message: '添加课程信息失败!'
                })
            })
        },
        // 当点击回到上一步时,数据回显
        updateCourse(id) {
           course.updateCourseInfo(this.courseInfo.id,this.courseInfo)
           .then(response => {
               this.$message({
                    type: 'success',
                    message: '添加课程信息成功!'
                })
                this.$router.push({path: '/course'})
           }).catch(response => {
               this.$message({
                    type: 'error',
                    message: '添加课程信息失败!'
                })
           })
        },
        getLevelAll() {
            subject.getAllSubjectList()
            .then(response => {
                this.oneLevelSubjectList = response.data.OneSubjectDto
            })
        },
        // 当点击某个课程类别,显示出该课程下的二级分类课程
        subjectLevelOneChanged(value) {
            //1 遍历所有一级分类集合
            //2 获取每个一级分类
            //3 判断value值和遍历出来的每个一级分类id值是否一样，
            //4 若相同则将该id下的二级分类都赋给twoLevelSubjectList对象
            for(var i=0;i < this.oneLevelSubjectList.length;i++) {
                var levelOne = this.oneLevelSubjectList[i];
                if(levelOne.id === value) {
                    this.twoLevelSubjectList = levelOne.children
                }
            }            
        },
        handleAvatarSuccess() {
            //上传成功之后，获取上传图片的oss的路径，赋值给课程封面字段
            this.courseInfo.cover = res.data.imgurl
        },
        beforeAvatarUpload() {
            //设置允许上传图片格式，上传图片大小
            const isJPG = file.type === 'image/jpeg'
            //M
            const isLt2M = file.size / 1024 / 1024 < 2

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!')
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!')
            }
            return isJPG && isLt2M
        },
        // 在点击返回上一步时数据回显
        getIdCourse(id) {
            course.getCourseInfo(id)
            .then(response => {
                this.courseInfo = response.data.courseInfoForm
                //1 查询所有的一级分类
                    subject.getAllSubjectList()
                        .then(response => {
                            this.oneLevelSubjectList = response.data.OneSubjectDto
                            //2 遍历一级分类集合
                            for(var i=0;i<this.oneLevelSubjectList.length;i++) {
                                //3 获取每个一级分类
                                var levelOne = this.oneLevelSubjectList[i]
                                //4 判断：每个一级分类id和课程所属一级分类id是否相同
                                if(levelOne.id === this.courseInfo.subjectParentId) {
                                    //5 获取这个一级分类里面的所有的二级分类
                                    this.twoLevelSubjectList = levelOne.children
                                }
                            }
                    })
                    //调用获取所有的讲师的方法
                    this.getTeacherList()
            })
        }
    }
}
</script>