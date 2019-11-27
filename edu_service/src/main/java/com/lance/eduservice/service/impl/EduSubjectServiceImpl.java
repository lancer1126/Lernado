package com.lance.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lance.eduservice.entity.EduSubject;
import com.lance.eduservice.mapper.EduSubjectMapper;
import com.lance.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2019-11-27
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //从excel表格中获取数据
    @Override
    public List<String> importSubject(MultipartFile file)
    {
        List<String> msg = new ArrayList<>();
        try
        {
            // 1.获取文件输入流
            InputStream in = file.getInputStream();

            // 2.创建workbook
            XSSFWorkbook workbook = new XSSFWorkbook(in);

            // 3.workbook获取sheet
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 4.sheet获取row
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++)
            {
                // 得到excel的每一行
                XSSFRow row = sheet.getRow(i);
                if (row == null)
                {
                    String str = "表格数据为空,请输入数据: ";
                    // TODO
                    return null;
                }

                //5.若行不为空,则通过row获取cell
                XSSFCell cellOne = row.getCell(0);

                if (cellOne == null)
                {
                    String error = "第"+i+"行添加失败";
                    msg.add(error);
                    continue;
                }

                //6.cell获取cell值
                String cellOneValue = cellOne.getStringCellValue();


                EduSubject existSubject = isExistSubjectOne(cellOneValue);
                String id_parent = "0";

                // 判断表中是否存在一级分类,如果没有则进行添加
                if (existSubject == null)
                {
                    EduSubject eduSubject = new EduSubject();
                    eduSubject.setTitle(cellOneValue);
                    eduSubject.setParentId("0");
                    eduSubject.setSort(0);
                    baseMapper.insert(eduSubject);
                    id_parent = eduSubject.getId();
                } else {
                    id_parent = existSubject.getId();
                }

                //获取第二列数据
                XSSFCell cellTwo = row.getCell(1);
                if (cellTwo == null)
                {
                    String error = "第"+i+"行添加失败";
                    msg.add(error);
                    continue;
                }
                String cellTwoValue = cellTwo.getStringCellValue();

                // 判断表中是否有二级分类
                EduSubject existSubjectTwo = isExistSubjectTwo(cellTwoValue, id_parent);
                if (existSubjectTwo == null)
                {
                    EduSubject eduSubject = new EduSubject();
                    eduSubject.setTitle(cellOneValue);
                    eduSubject.setParentId(id_parent);
                    eduSubject.setSort(0);
                    baseMapper.insert(eduSubject);
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }

    // 判断数据库中是否存在一级分类
    private EduSubject isExistSubjectOne(String name)
    {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id","0");
        return baseMapper.selectOne(queryWrapper);
    }

    // 判断是否存在二级分类
    private EduSubject isExistSubjectTwo(String name,String parentId)
    {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id", parentId);
        return baseMapper.selectOne(queryWrapper);
    }
}







