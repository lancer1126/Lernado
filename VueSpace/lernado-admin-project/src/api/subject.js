import request from '@/utils/request'

export default {
    getAllSubjectList() {
        return request({
            url: '/eduservice/subject',
            method: 'get'
          })
    },
    removeSubjectId(id) {
        return request({
            url: '/eduservice/subject/'+id,
            method: 'delete'
        })
    },
    addSubjectOne(subject) {
        return request({
            url: '/eduservice/subject/addOneLevel',
            method: 'post',
            data:subject
        })
    },
    addSubjectTwo(subject) {
        return request({
            url: '/eduservice/subject/addTwoLevel',
            method: 'post',
            data:subject
        })
    }
}