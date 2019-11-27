package com.lance.eduservice.controller;


import com.lance.common.R;
import com.lance.eduservice.entity.EduSubject;
import com.lance.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2019-11-27
 */
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController
{
    @Autowired
    EduSubjectService eduSubjectService;

    //从excel表格中获取数据并添加到数据库中
    @PostMapping("import")
    public R importExeclSubject(@RequestParam("file") MultipartFile file)
    {
        List<String> msg = eduSubjectService.importSubject(file);
        return R.ok().data("msgList",msg);
    }
}

