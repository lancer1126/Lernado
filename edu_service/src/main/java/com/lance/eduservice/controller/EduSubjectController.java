package com.lance.eduservice.controller;


import com.lance.common.R;
import com.lance.eduservice.entity.EduSubject;
import com.lance.eduservice.entity.dto.OneSubjectDto;
import com.lance.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
public class EduSubjectController
{
    @Autowired
    private EduSubjectService eduSubjectService;

    //1.从excel表格中获取数据并添加到数据库中
    @PostMapping("import")
    public R importExeclSubject(@RequestParam("file") MultipartFile file)
    {
        List<String> msg = eduSubjectService.importSubject(file);

        if (msg.size() == 0)
            return R.ok();
        else
            return R.error().data("msgList",msg);
    }

    //2.返回所有的分类数据,使用json格式
    @GetMapping
    public R getAllSubjectList()
    {
        List<OneSubjectDto> list = eduSubjectService.getSubjectList();
        return R.ok().data("OneSubjectDto",list);
    }

    //3.删除一级分类
    @DeleteMapping("{id}")
    public R deleteSubjectById(@PathVariable String id)
    {
        boolean flag = eduSubjectService.deleteSubjectById(id);
        if (flag)
            return R.ok();
        else
            return R.error();
    }

    //4.添加一级分类
    @PostMapping("addOneLevel")
    public R addOneLevel(@RequestBody EduSubject eduSubject)
    {
        boolean flag = eduSubjectService.saveOneLevel(eduSubject);
        if (flag)
            return R.ok();
        else
            return R.error();
    }

    //5 添加二级分类
    @PostMapping("addTwoLevel")
    public R addTwoLevel(@RequestBody EduSubject eduSubject) {
        boolean flag = eduSubjectService.saveTwoLevel(eduSubject);
        if(flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }
}






















