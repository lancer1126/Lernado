package com.lance.eduservice.controller;


import com.lance.common.R;
import com.lance.eduservice.entity.EduVideo;
import com.lance.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2019-12-03
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController
{
    @Autowired
    private EduVideoService eduVideoService;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo)
    {
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    //根据id查询
    @GetMapping("{videoId}")
    public R getVideoId(@PathVariable String videoId)
    {
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("eduVideo",eduVideo);
    }

    // 修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo)
    {
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    // 删除操作
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId)
    {
        eduVideoService.removeById(videoId);
        return R.ok();
    }
}

