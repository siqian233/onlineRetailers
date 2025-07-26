package com.lightning.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@PropertySource("classpath:application.yml") 如果配置文件名称不是 application.yml，则需要使用 @PropertySource
@Configuration
@Data
@ConfigurationProperties(prefix = "aliyun.oss") //自动配置注解: 读取默认配置文件  application.yml中的属性  aliyun.oss
public class OssConfig {
    //@Value("${aliyun.oss.endpoint}")
    private String endpoint;
    //@Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    //@Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    //@Value("${aliyun.oss.bucketName}")
    private String bucketName;
    //@Value("${aliyun.oss.urlPrefix}")
    private String urlPrefix;

    @Bean // 将 OSSClient 注册为 Spring Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}

