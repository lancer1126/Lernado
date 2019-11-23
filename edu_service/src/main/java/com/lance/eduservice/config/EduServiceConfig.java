package com.lance.eduservice.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.lance.eduservice.mapper")
@EnableTransactionManagement
@Configuration
public class EduServiceConfig
{
    @Bean
    public ISqlInjector sqlInjector()
    { return new LogicSqlInjector(); }
}
