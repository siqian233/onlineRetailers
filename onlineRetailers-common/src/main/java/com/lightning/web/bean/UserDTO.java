package com.lightning.web.bean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long userId;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3到20之间")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度必须在6到50之间")
    private String userPassword;

    private Set<String> roles;

    private String email;

    private String avatar;

    private MultipartFile avatarFile;

    private String mobile;

    private Integer gender;

    private Integer userStatus;

    // 用于快速注册
    public UserDTO(String username, String userPassword) {
        this.userName = username;
        this.userPassword = userPassword;
    }
    // 用于快速注册,带角色
    public UserDTO(String username, String userPassword, Set<String> roles) {
        this.userName = username;
        this.userPassword = userPassword;
        this.roles = roles;
    }
    // 用于带所有的参数注册
    public UserDTO(String username, String userPassword, Set<String> roles, String mobile, String email,
                   MultipartFile avatarFile, Integer gender, Integer userStatus) {
        this.userName = username;
        this.userPassword = userPassword;
        this.roles = roles;
        this.mobile = mobile;
        this.email = email;
        this.avatarFile = avatarFile;
        this.gender = gender;
        this.userStatus = userStatus;
    }

    // 用于更新或查询
    public UserDTO(Long userId, String username, Set<String> roles) {
        this.userId = userId;
        this.userName = username;
        this.roles = roles;
    }

    //用于根据 userId更新全部数据
    public UserDTO(Long userId, String username, String userPassword, Set<String> roles,
                   String mobile, String email, MultipartFile avatarFile, Integer gender, Integer userStatus) {
        this.userId = userId;
        this.userName = username;
        this.userPassword = userPassword;
        this.roles = roles;
        this.mobile = mobile;
        this.email = email;
        this.avatarFile = avatarFile;
        this.gender = gender;
        this.userStatus = userStatus;
    }
}
