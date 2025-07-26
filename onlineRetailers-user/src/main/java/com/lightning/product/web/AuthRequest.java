package com.lightning.product.web;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 认证请求 DTO (用于登录)。
 */
@Data
public class AuthRequest {
    @NotBlank(message = "userName cannot be empty")
    private String userName;

    @NotBlank(message = "userPassword cannot be empty")
    private String userPassword;
}

