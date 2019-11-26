import request from '@/utils/request'   // 引入模版提供的组件

export default {
    // 分页条件查询的方法
    getTeacherPageList(page,limit,searchObj) {
        return request ({
            // 后端controller中multiCondition方法的路径
            url: '/eduservice/teacher/multiConditionPageList/'+page+'/'+limit,
            method: 'post',
            data: searchObj
        })
    },
    deleteTeacherById(id) {
        return request ({
            //后端controller中删除方法的路径
            url: '/eduservice/teacher/'+id,
            method: 'delete'
        })
    },
    addTeacher(teacher) {
        return request ({
            url: '/eduservice/teacher/addTeacher',
            method: 'post',
            data: teacher
        })
    }
}
