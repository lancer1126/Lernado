package com.lance.eduservice.controller;


import com.lance.common.R;
import com.lance.eduservice.entity.EduChapter;
import com.lance.eduservice.entity.dto.EduChapterDto;
import com.lance.eduservice.service.EduChapterService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2019-12-03
 */
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController
{
    @Autowired
    private EduChapterService eduChapterService;

    //1.根据id获取课程章节信息
    @GetMapping("getChapterVideoList/{courseId}")
    public R getChapterVideoListCourseId(@PathVariable String courseId)
    {
        List<EduChapterDto> list = eduChapterService.getChapterVideoListByCourseId(courseId);
        return R.ok().data("items",list);
    }

    //2.添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter)
    {
        boolean flag = eduChapterService.save(eduChapter);
        if (flag)
            return R.ok();
        else
            return R.error();
    }

    //3.根据id查询章节
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId)
    {
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return R.ok().data("eduChapter",eduChapter);
    }

    //4.根据id修改章节内容
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter)
    {
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    //5.删除章节
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId)
    {
        Boolean flag = eduChapterService.removeChapterID(chapterId);
        if (flag)
            return R.ok();
        else
            return R.error();
    }
}






