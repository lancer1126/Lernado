package com.lance.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lance.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lance.eduservice.entity.query.QueryTeacher;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2019-11-23
 */
public interface EduTeacherService extends IService<EduTeacher>
{
    void pageListCondition(Page<EduTeacher> pageTeacher, QueryTeacher queryTeacher);
    boolean deleteTeacherById(String id);

}
