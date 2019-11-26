package com.lance.eduservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lance.common.R;
import com.lance.eduservice.entity.EduTeacher;
import com.lance.eduservice.entity.query.QueryTeacher;
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
// 解决跨域问题
@CrossOrigin
public class EduTeacherController
{
    @Autowired
    private EduTeacherService eduTeacherService;

    @PostMapping("login")
    public R login()
    { return R.ok().data("token","admin"); }

    @GetMapping("info")
    public R info() {
        return R.ok().data("roles","[admin]").data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    // 1.获得所有的讲师列表
    @GetMapping
    public R getAllTeacherList()
    {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    // 2.分页查询讲师列表
    @GetMapping("pageList/{page}/{limit}")
    public R getPageTeacherList(@PathVariable Long page, @PathVariable Long limit)
    {
        // 创建page对象，传递两个参数
        Page<EduTeacher> pageTeacher = new Page<>(page,limit);

        // 调用方法分页查询
        eduTeacherService.page(pageTeacher,null);

        // 从pageTeacher对象中获取分页数据
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return  R.ok().data("total",total).data("items",records);
    }

    // 3.多条件组合查询
    @PostMapping("multiConditionPageList/{page}/{limit}")
    public R getMultiConditionPageList(@PathVariable Long page, @PathVariable Long limit,
                                       @RequestBody QueryTeacher queryTeacher)
    {
        Page<EduTeacher> pageTeacher = new Page<>(page,limit);

        eduTeacherService.pageListCondition(pageTeacher,queryTeacher);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("items",records);
    }

    // 4.添加讲师的方法
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save)
            return R.ok();
        else
            return R.error();
    }

    // 5. 根据ID查询讲师
    @GetMapping("getTeacherInfo/{id}")
    public R getTeacherInfo(@PathVariable String id)
    {
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }

    //6. 根据ID修改讲师
    @PostMapping("updateTeacher/{id}")
    public R updateTeacher(@PathVariable String id,@RequestBody EduTeacher eduTeacher)
    {
        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b)
            return R.ok();
        else
            return R.error();
    }

    // 7.根据id删除讲师（逻辑删除，并非真正的删除）
    @DeleteMapping("{id}")
    public R deleteTeacherById(@PathVariable String id)
    {
        boolean flag = eduTeacherService.removeById(id);
        if (flag)
            return R.ok();
        else
            return R.error();
    }
}





























