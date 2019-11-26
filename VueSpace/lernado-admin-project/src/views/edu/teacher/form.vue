<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name"/>
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" controls-position="right" min="0"/>
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career"/>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
      </el-form-item>

      <!-- 讲师头像：TODO -->

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import teacher from '@/api/teacher'

const defaultForm = {
    name: '',
    sort: 0,
    level: '',
    career: '',
    intro: '',
    avatar: ''
}

export default {
    data() {
        return {
            // 设置teacher对象的内容
            teacher: defaultForm
        }
    },
    watch: {
        $route(to,from) {
            console.log('watch $route')
            this.init()
        }
    },
    created() {
        this.init()
    },
    methods: {
        init() {
            // 在页面加载之前,判断路由中是否有id值,
            //有则为修改,调用方法根据id查询
            //没有则为新增讲师
            if(this.$route.params && this.$route.params.id) {
                const id = this.$route.params.id
                this.getTeacherById(id)
            } else {
                // 显示一个空表单
                this.teacher = {...defaultForm}
            }
        },
        saveOrUpdate() {
            if(!this.teacher.id) {
                this.saveTeacher()
            } else {
                this.updateTeacher()
            }
        },
        // 添加讲师,从teacher的api中获取addTeacher方法来进行添加
        saveTeacher() {
            teacher.addTeacher(this.teacher)
                .then(() => {
                    return this.$message({
                        type: 'success',
                        message: '添加成功'
                    })
                }).then(() => {
                    this.$router.push({path: '/teacher'})
                }).catch(() => {
                    this.$message({
                        type: 'error',
                        message: '添加失败'
                    })
                })
        },
        getTeacherById(id) {
            // 从teacher的api中获取到该id所代表的讲师数据
            teacher.getTeacherId(id)
                .then(response => {
                    this.teacher = response.data.eduTeacher
                })
        },
        // 使用teacher的api的update方法修改讲师的资料
        updateTeacher() {
            teacher.updateTeacherId(this.teacher.id,this.teacher)
                .then(() => {
                    return this.$message({
                        type: 'success',
                        message: '修改成功'
                    })
                }).then(() => {
                    // 路由跳转
                    this.$router.push({path: '/teacher'})
                }).catch(() =>{
                    this.$message({
                        type: 'error',
                        message: '修改失败'
                    })
                })
        }
    }
}

</script>

