package com.lightning.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.lightning.web.api")
@MapperScan("com.lightning.product.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class ProductApp {
    public static void main( String[] args ) {
        SpringApplication.run(ProductApp.class, args);
    }
}
