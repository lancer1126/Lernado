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
    },
    getCourseInfo(id) {
        return request({
            url: '/eduservice/course/getCourseInfo/'+id,
            method: 'get'
        })
    },
    updateCourseInfo(id,courseInfo) {
        return request({
            url: '/eduservice/course/updateCourseInfo/'+id,
            method: 'post',
            data: courseInfo
        })
    }
}