package com.lightning.product.config;

import com.lightning.Filters.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置类。
 * 启用 Web 安全和方法级别的安全（@PreAuthorize）。
 */
@Configuration
@EnableWebSecurity  // 启用 Web 安全
@EnableMethodSecurity // 启用方法级别的安全
public class SecurityConfig {
    /**
     * 配置密码编码器
     * 使用BCryptPasswordEncoder进行密码编码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 自定义JWT授权过滤器Bean
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }

    /**
     * 配置安全过滤器链。
     * 定义了哪些请求需要认证/授权，以及自定义过滤器的顺序。
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable)//取消默认登录页面的使用
                .logout(AbstractHttpConfigurer::disable)//取消默认登出页面的使用
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF 保护，因为我们使用 JWT，不依赖 Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 设置会话管理策略为无状态，不创建和使用 Session
                .httpBasic(AbstractHttpConfigurer::disable)//禁用httpBasic，因为我们传输数据用的是post，而且请求体是JSON
                .authorizeHttpRequests(authorize -> authorize
                        // 允许所有用户访问认证相关的端点（登录、注册）
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )
                // 在 Spring Security 的 UsernamePasswordAuthenticationFilter 之前添加我们的 JWT 授权过滤器
                // 这确保了在 Spring Security 尝试进行认证之前，SecurityContextHolder 已经被我们的过滤器填充
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
