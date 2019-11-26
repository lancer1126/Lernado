package com.lance.eduservice.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.lance.common.R;
import net.bytebuddy.asm.Advice;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

// 用作上传文件到阿里云OSS
@RestController
@RequestMapping("/eduservice/oss")
@CrossOrigin
public class FileUploadController
{
    @PostMapping("/upload")
    public R uploadTeacherImg(@RequestParam("file") MultipartFile file) throws IOException
    {
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "LTAI4FrAhXnANcNsQyjs4fz8";
        String accessKeySecret = "tJybPmIsuM8oTNZ2mdNvPNJx2ABKCx";
        String bucketName = "lernado-file";

        // 获取上传的文件名称和输入流
        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        // 创建OSSclient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);

       //上传文件到指定的存储空间
        ossClient.putObject(bucketName,filename,inputStream);

        // 关闭OSS
        ossClient.shutdown();

        // 返回文件路径
        String path = "http://"+bucketName+"."+endpoint+"/"+filename;
        return  R.ok().data("imgurl",path);
    }

}
