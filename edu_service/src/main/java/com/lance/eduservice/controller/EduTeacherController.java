package com.lance.eduservice.controller;

import com.lance.common.R;
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
    public R getAllTeacherList() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    @DeleteMapping("{id}")
    public boolean deleteTeacherById(@PathVariable("id") String id) {
        return eduTeacherService.removeById(id);
    }
}

