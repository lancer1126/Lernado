package com.lance.eduservice.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EduChapterDto
{
    private String id;
    private String title;
    private List<EduVideoDto> children = new ArrayList<>();
}
