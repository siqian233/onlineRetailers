package com.lightning.product.web;

import com.lightning.web.bean.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应 DTO (用于登录成功后返回 Token 和用户信息)。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UserDTO myUserDTO;
}

