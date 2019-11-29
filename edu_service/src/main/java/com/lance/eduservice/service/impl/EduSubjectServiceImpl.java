package com.lance.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lance.eduservice.entity.EduSubject;
import com.lance.eduservice.entity.dto.OneSubjectDto;
import com.lance.eduservice.entity.dto.TwoSubjectDto;
import com.lance.eduservice.mapper.EduSubjectMapper;
import com.lance.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
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


                EduSubject existSubject = existOneSubject(cellOneValue);
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
                EduSubject existSubjectTwo = existTwoSubject(cellTwoValue, id_parent);
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



    //返回所有的分类，封装要求的json格式
    @Override
    public List<OneSubjectDto> getSubjectList()
    {
        //1 查询所有一级分类
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id","0");
        List<EduSubject> allOneSubjects = baseMapper.selectList(wrapper1);

        //2 查询所有二级分类
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id","0");
        List<EduSubject> allTwoSubjects = baseMapper.selectList(wrapper2);

        //创建list集合，用于存储所有一级分类
        List<OneSubjectDto> oneSubjectDtolist = new ArrayList<>();
        //3 首先构建一级分类
        //遍历所有的一级分类，得到每个EduSubject对象，把每个EduSubject对象转换OneSubjectDto
        for (int i = 0; i < allOneSubjects.size(); i++)
        {
            //获取每个EduSubject对象
            EduSubject eduOneSubject = allOneSubjects.get(i);
            //创建OneSubjectDto对象
            OneSubjectDto oneSubjectDto = new OneSubjectDto();
            //把每个EduSubject对象转换OneSubjectDto
            BeanUtils.copyProperties(eduOneSubject,oneSubjectDto);
            //把dto对象放到list集合
            oneSubjectDtolist.add(oneSubjectDto);

            //获取一级分类所有二级分类，List<TwoSubjectDto>
            //把所有的二级分类添加到每个一级分类对象中oneSubjectDto.setChildren(list);
            //创建list集合，用于存储二级分类
            List<TwoSubjectDto>  twoSubjectDtoList = new ArrayList<>();
            //遍历所有的二级分类，得到每个二级分类
            for (int m = 0; m < allTwoSubjects.size(); m++)
            {
                //得到每个二级分类
                EduSubject eduTwoSubject = allTwoSubjects.get(m);
                //判断一级分类id和二级分类parentid是否一样
                if(eduTwoSubject.getParentId().equals(eduOneSubject.getId()))
                {
                    //二级分类转换TwoSubjectDto
                    TwoSubjectDto twoSubjectDto = new TwoSubjectDto();
                    //内省  反射
                    BeanUtils.copyProperties(eduTwoSubject,twoSubjectDto);
                    //放到list集合
                    twoSubjectDtoList.add(twoSubjectDto);
                }
            }
            //把二级分类放到每个一级分类中
            oneSubjectDto.setChildren(twoSubjectDtoList);
        }
        return oneSubjectDtolist;
    }

    // 根据id删除条目
    @Override
    public boolean deleteSubjectById(String id)
    {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);

        // 判断是当前一级目录下是否有二级的分类,如果没有则进行删除
        if (count > 0)
            return  false;
        else
            return (baseMapper.deleteById(id)) > 0;
    }

    // 保存一级目录
    @Override
    public boolean saveOneLevel(EduSubject eduSubject)
    {
        //判断一级分类是否存在，如果存在添加
        EduSubject eduSubjectExist = this.existOneSubject(eduSubject.getTitle());
        if(eduSubjectExist == null) {//不存在
            //添加
            //一级分类parentid=0
            eduSubject.setParentId("0");
            int result = baseMapper.insert(eduSubject);
            return result>0;
        }
        return false;
    }

    @Override
    public boolean saveTwoLevel(EduSubject eduSubject)
    {
        //判断二级分类是否存在
        EduSubject eduSubjectExist = this.existTwoSubject(eduSubject.getTitle(), eduSubject.getParentId());
        if(eduSubjectExist == null) {//不存在
            //添加
            int insert = baseMapper.insert(eduSubject);
            return insert>0;
        }
        return false;
    }


    // 判断数据库中是否存在一级分类
    private EduSubject existOneSubject(String name)
    {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id","0");
        return baseMapper.selectOne(queryWrapper);
    }

    // 判断是否存在二级分类
    private EduSubject existTwoSubject(String name, String parentId)
    {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id", parentId);
        return baseMapper.selectOne(queryWrapper);
    }
}













