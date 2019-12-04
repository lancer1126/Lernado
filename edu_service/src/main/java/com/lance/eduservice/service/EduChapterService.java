package com.lance.eduservice.service;

import com.lance.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lance.eduservice.entity.dto.EduChapterDto;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2019-12-03
 */
public interface EduChapterService extends IService<EduChapter> {

    List<EduChapterDto> getChapterVideoListByCourseId(String courseId);


    Boolean removeChapterID(String chapterId);
}
