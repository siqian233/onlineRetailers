package com.lightning.web;

import com.lightning.service.UserService;
import com.lightning.web.bean.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 用户管理 REST API 控制器。
 */
@RestController
@RequestMapping("/users") // 所有用户管理相关的接口都以 /users 开头
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 查询所有用户（仅限管理员）。
     * URL: GET /users
     * 权限：只有拥有 'ADMIN' 角色的用户才能访问。
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据ID查询用户信息（管理员可以查询所有，普通用户只能查询自己）。
     * URL: GET /users/{id}
     * 权限：拥有 'ADMIN' 角色，或者请求的用户ID与路径中的ID一致。
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        // 获取当前认证用户的ID (从 Gateway 传递的 X-User-ID 头中获取)
        String currentUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 如果不是管理员且请求的ID不是当前用户ID，则拒绝访问
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) && !currentUserId.equals(String.valueOf(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found
    }

    /**
     * 更新用户信息（管理员可以更新所有，普通用户只能更新自己）。
     * URL: PUT /users/{id}
     * 权限：拥有 'ADMIN' 角色，或者请求的用户ID与路径中的ID一致。
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        // 获取当前认证用户的ID (从 Gateway 传递的 X-User-ID 头中获取)
        String currentUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 如果不是管理员且请求的ID不是当前用户ID，则拒绝访问
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) && !currentUserId.equals(String.valueOf(id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }
    }
}

