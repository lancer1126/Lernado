import request from '@/utils/request'   // 引入模版提供的组件

export default {
    // 分页条件查询的方法
    getTeacherPageList(page,limit,searchObj) {
        return request ({
            url: '/eduservice/teacher/multiConditionPageList/'+page+'/'+limit,
            method: 'post',
            data: searchObj
        })
    }
}
