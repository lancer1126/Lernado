package com.lance.eduservice.controller;


import com.lance.eduservice.entity.EduTeacher;
import com.lance.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2019-11-23
 */
@RestController
@RequestMapping("/eduservice/teacher")
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;

    @GetMapping
    public List<EduTeacher> getAllTeacherList() {
        System.out.println("-------执行到了这里------");
        List<EduTeacher> list = eduTeacherService.list(null);
        return list;
    }

    @DeleteMapping("{id}")
    public boolean deleteTeacherById(@PathVariable("id") String id) {
        return eduTeacherService.removeById(id);
    }
}

