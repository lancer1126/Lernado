package com.lance.eduservice.controller;


import com.lance.common.R;
import com.lance.eduservice.entity.form.CourseInfoForm;
import com.lance.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController
{
    @Autowired
    EduCourseService eduCourseService;

    // 从网页中获取信息添加信息到数据库中
    // 在发布新课程页面中添加的信息传递给courseInfoForm对象
    @PostMapping
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm)
    {
        String courseId = eduCourseService.insertCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId);
    }

    // 通过课程的id获取课程的信息
    @GetMapping("getCourseInfo/{id}")
    public R getCourseInfo(@PathVariable String id)
    {
        CourseInfoForm courseInfoForm = eduCourseService.getIdCourse(id);
        return R.ok().data("courseInfoForm",courseInfoForm);
    }

    // 修改课程信息
    @PostMapping("updateCourseInfo/{id}")
    public R updateCourseInfo(@PathVariable String id,@RequestBody CourseInfoForm courseInfoForm)
    {
        Boolean flag = eduCourseService.updateCourse(courseInfoForm);
        if(flag)
            return R.ok();
        else
            return R.error();
    }
}

