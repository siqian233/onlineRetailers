//package com.lightning.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//public class GatewayCorsConfig {
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        // 允许的源
//        corsConfig.setAllowedOrigins(List.of("*"));
//        // 允许的HTTP方法
//        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        // 允许所有请求头
//        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
//        // 预检请求（OPTIONS请求）的缓存时间，单位秒
//        // 在此时间内，浏览器无需再发送预检请求
//        corsConfig.setMaxAge(3600L);
//        // 将CORS配置注册到所有路径
//        source.registerCorsConfiguration("/**", corsConfig);
//        // 返回CORS Web过滤器
//        return new CorsWebFilter(source);
//    }
//}
