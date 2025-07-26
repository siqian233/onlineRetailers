package com.lightning.product.web;

import com.lightning.product.service.UserService;
import com.lightning.web.bean.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证相关的 REST API 控制器（登录、注册、注销）。
 * 这些接口在 SecurityConfig 中被设置为 permitAll()，无需认证。
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册。
     * URL: POST /auth/register
     * 无需认证。
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser); // 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict
        }
    }

    /**
     * 检查用户名是否存在。
     * URL: GET /auth/check-username?username=testuser
     * 无需认证。
     */
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam String username) {
        boolean exists = userService.checkUsernameExists(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * 用户登录。
     * URL: POST /auth/login
     * 无需认证。
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse response = userService.loginUser(authRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()); // 401 Unauthorized
        }
    }


    /**
     * 注销登录状态（通常是客户端行为，服务端无状态）。
     * URL: POST /auth/logout
     * 无需认证。
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        // 对于无状态 JWT，注销通常意味着客户端删除其本地存储的 Token。
        // 如果需要服务器端强制注销（例如，将 Token 加入黑名单），可以在 JwtTokenUtil 中实现。
        userService.logout(token);
        return ResponseEntity.ok("Logged out successfully (client-side token deletion).");
    }
}
