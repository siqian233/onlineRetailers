package com.lightning.Filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    // Gateway 传递用户ID的请求头名称
    private static final String USER_ID_HEADER = "X-User-ID";
    // Gateway 传递用户角色（逗号分隔）的请求头名称
    private static final String USER_ROLES_HEADER = "X-User-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader(USER_ID_HEADER);
        String userRoles = request.getHeader(USER_ROLES_HEADER);

        if( userId != null&&!userId.trim().isEmpty() && userRoles != null && !userRoles.trim().isEmpty()){
            // 将逗号分隔的角色字符串转换为 GrantedAuthority 集合
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(userRoles.split(","))
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase())) // Spring Security 角色前缀为 ROLE_
                            .collect(Collectors.toList());
            // 创建认证令牌。这里使用 userId 作为 principal，密码为 null，并传入权限列表。
            // 密码为 null 是因为认证已经在 Gateway 完成，这里只是设置授权信息。
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            // 将认证对象设置到 SecurityContextHolder，供后续的授权检查使用
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
