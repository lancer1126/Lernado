package com.lance.eduservice.service;

import com.lance.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lance.eduservice.entity.dto.OneSubjectDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2019-11-27
 */
public interface EduSubjectService extends IService<EduSubject> {

    List<String> importSubject(MultipartFile file);

    List<OneSubjectDto> getSubjectList();

    boolean deleteSubjectById(String id);

    boolean saveOneLevel(EduSubject eduSubject);

    boolean saveTwoLevel(EduSubject eduSubject);
}
