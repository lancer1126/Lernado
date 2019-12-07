package com.lance.eduservice.entity.dto;

import com.sun.xml.bind.v2.model.core.ID;
import lombok.Data;

// 封装一个课程所有的信息
@Data
public class CourseInfoDto
{
    private String id;
    private String title;
    private String cover;
    private String price;
    private String description;
    private String teacherName;
    private String levelOne;    // 一级名称
    private String levelTwo;    //二级名称
}
