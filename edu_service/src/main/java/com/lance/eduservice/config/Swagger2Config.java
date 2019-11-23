package com.lance.eduservice.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger2Config
{
    public Docket webApiConfig()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi").apiInfo(webApiInfo())
                .select().build();
    }

    private ApiInfo webApiInfo()
    {
        return new ApiInfoBuilder().title("网站-课程中心API文档").description("描述了微服务接口定义")
                .version("1.0").contact(new Contact("lernado","https://lernado.com","123456@123.com")).build();
    }
}
