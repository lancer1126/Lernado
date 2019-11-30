import request from '@/utils/request'

export default {
    addCourseInfo(courseInfo) {
        return request({
            url: "/eduservice/course",
            method: 'post',
            data: courseInfo
        })
    },
    getAllTeacherList() {
        return request({
            url: '/eduservice/teacher',
            method: 'get'
        })
    }
}