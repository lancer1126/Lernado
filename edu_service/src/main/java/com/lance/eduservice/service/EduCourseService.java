package com.lance.eduservice.service;

import com.lance.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lance.eduservice.entity.dto.CourseInfoDto;
import com.lance.eduservice.entity.form.CourseInfoForm;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2019-11-29
 */
public interface EduCourseService extends IService<EduCourse> {

    String insertCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getIdCourse(String id);

    Boolean updateCourse(CourseInfoForm courseInfoForm);

    CourseInfoDto getCourseInfoAll(String courseId);
}
