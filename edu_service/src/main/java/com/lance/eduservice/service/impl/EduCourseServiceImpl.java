package com.lance.eduservice.service.impl;

import com.lance.eduservice.entity.EduCourse;
import com.lance.eduservice.entity.EduCourseDescription;
import com.lance.eduservice.entity.dto.CourseInfoDto;
import com.lance.eduservice.entity.form.CourseInfoForm;
import com.lance.eduservice.mapper.EduCourseMapper;
import com.lance.eduservice.service.EduCourseDescriptionService;
import com.lance.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2019-11-29
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService
{

    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;

    // 从网页中获取信息并添加课程到数据库中
    // 从添加课程界面输入信息传递给courseInfoForm对象
//    @Transactional
    @Override
    public String insertCourseInfo(CourseInfoForm courseInfoForm)
    {
        // 将保存着课程信息的courseInfoForm对象复制给eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);

        // 将获得课程信息的eduCourse对象添加到数据库中
        baseMapper.insert(eduCourse);

        // 将课程的描述信息添加到描述表中
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        // 从courseInfoForm获取到描述信息并传给eduCourseDescription
        eduCourseDescription.setDescription(courseInfoForm.getDescription());

        // 在eduCourse获取到网页传输的信息后,会产生一个id值,将之传给描述信息
        String courseId = eduCourse.getId();
        eduCourseDescription.setId(courseId);

        // 最后由eduCourseDescriptionService将描述信息传到表中
        boolean save = eduCourseDescriptionService.save(eduCourseDescription);
        if (save)
            return courseId;
        else
            return "数据传输错误";
    }

    // 通过课程的id获取课程的信息
    @Override
    public CourseInfoForm getIdCourse(String id)
    {
        // 获取课程基本信息
        EduCourse eduCourse = baseMapper.selectById(id);
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse,courseInfoForm);

        // 获取课程描述信息
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(id);
        String description = eduCourseDescription.getDescription();
        courseInfoForm.setDescription(description);
        return courseInfoForm;
    }

    // 更改课程信息
    @Override
    public Boolean updateCourse(CourseInfoForm courseInfoForm)
    {
        //修改基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        baseMapper.updateById(eduCourse);

        // 修改课程描述信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoForm.getId());
        eduCourseDescription.setDescription(courseInfoForm.getDescription());

        return eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    @Override
    public CourseInfoDto getCourseInfoAll(String courseId)
    {
        return null;
    }
}
