//package com.lightning.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")   // 允许跨域访问的路径
//                .allowedOriginPatterns("*")   // 允许跨域访问的源
//                .allowedMethods("*") // 允许请求方法
//                .allowedHeaders("*") // 允许头部设置
//                .allowCredentials(true) // 是否发送cookie
//                .maxAge(3600); // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
//    }
//}
