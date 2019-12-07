import request from '@/utils/request'

export default {
    //添加的方法
    addVideo(video) {
        return request({
            url: '/eduservice/video/addVideo',
            method: 'post',
            data: video
        })
    },
    //根据id查询的方法
    getVideoInfo(videoId) {
        return request({
            url: '/eduservice/video/'+videoId,
            method: 'get'
        })
    },
    //修改的方法
    updateVideo(video) {
        return request({
            url: '/eduservice/video/updateVideo',
            method: 'post',
            data: video
        })
    },
    //删除的方法
    deleteVideo(videoId) {
        return request({
            url: '/eduservice/video/'+videoId,
            method: 'delete'
        })
    }
}