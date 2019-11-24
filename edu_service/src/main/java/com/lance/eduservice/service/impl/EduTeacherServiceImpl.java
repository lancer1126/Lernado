package com.lance.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lance.eduservice.entity.EduTeacher;
import com.lance.eduservice.entity.query.QueryTeacher;
import com.lance.eduservice.mapper.EduTeacherMapper;
import com.lance.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2019-11-23
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService
{
    @Override
    public void pageListCondition(Page<EduTeacher> pageTeacher, QueryTeacher queryTeacher)
    {
        if (queryTeacher == null)
        {
            baseMapper.selectPage(pageTeacher,null);
            return;
        }

        String name = queryTeacher.getName();
        String level = queryTeacher.getLevel();
        String begin = queryTeacher.getBegin();
        String end = queryTeacher.getEnd();

        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 拼接条件
        if (!StringUtils.isEmpty(name))
        { wrapper.like("name",name);}
        if (!StringUtils.isEmpty(level))
        { wrapper.eq("level",level); }
        if (!StringUtils.isEmpty(begin))
        { wrapper.ge("gmt_create",begin); }
        if (!StringUtils.isEmpty(end))
        { wrapper.le("gmt_create",end); }

        baseMapper.selectPage(pageTeacher,wrapper);
    }
}
