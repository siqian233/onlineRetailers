package com.lightning.product.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lightning.product.mapper.UserMapper;
import com.lightning.model.User;
import com.lightning.product.web.AuthRequest;
import com.lightning.product.web.AuthResponse;
import com.lightning.web.api.FileUploadApi;
import com.lightning.web.api.IdGeneratorApi;
import com.lightning.web.bean.ResponseResult;
import com.lightning.web.bean.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户业务逻辑服务。
 */
@Service
public class UserService {

    private final UserMapper userMapper; // 注入 UserMapper
    private final PasswordEncoder passwordEncoder;   //spring security提供的密码的加密器
    private final JwtTokenUtil jwtTokenUtil;    // 生成和验证token的工具类

    @Autowired
    private IdGeneratorApi idGeneratorApi;
    @Autowired
    private FileUploadApi fileUploadApi;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 用户注册。
     *
     * @param userDTO 注册信息
     * @return 注册成功的用户DTO
     * @throws IllegalArgumentException 如果用户名已存在
     */
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if (checkUsernameExists(userDTO.getUserName())) { // 使用新的 checkUsernameExists
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUserName());
        }

        //通过 feign客户端获得用户id
        ResponseResult rr = this.idGeneratorApi.getNextId();
        ResponseResult rr2 = this.fileUploadApi.upload(new MultipartFile[]{userDTO.getAvatarFile()});
        if (rr2.getCode() != 1) {
            throw new RuntimeException("上传头像失败");
        }
        List<String> avatarUrls = (List<String>) rr2.getData();

        User user = new User();
        user.setUserId(Long.parseLong(rr.getData().toString()));
        user.setUserName(userDTO.getUserName());
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword())); // 密码加密存储
        user.setMobile(userDTO.getMobile());
        user.setEmail(userDTO.getEmail());
        user.setGender(userDTO.getGender());
        user.setUserStatus(0);
        user.setRoles("CUST"); // 默认角色// 默认注册为普通用户，角色存储为逗号分隔的字符串
        user.setAvatar(avatarUrls.get(0));

        userMapper.insert(user); // 使用 MyBatis Plus 的 insert 方法
        return convertToDto(user); // 返回转换后的DTO
    }

    /**
     * 用户登录。
     *
     * @param authRequest 登录请求（用户名和密码）
     * @return 包含 JWT Token 和用户信息的响应
     * @throws IllegalArgumentException 如果用户名或密码不正确
     */
    public AuthResponse loginUser(AuthRequest authRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", authRequest.getUserName());
        User user = userMapper.selectOne(queryWrapper); // 使用 MyBatis Plus 的 selectOne

        if (user == null || !passwordEncoder.matches(authRequest.getUserPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        // 登录成功，生成 JWT Token
        // 将逗号分隔的角色字符串转换为 Set<String> 传递给 JWT 工具
        Set<String> rolesSet = Arrays.stream(user.getRoles().split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        String token = jwtTokenUtil.generateToken(user.getUserId(), user.getUserName(), rolesSet);
        return new AuthResponse(token, convertToDto(user));
    }

    /**
     * 判断用户名是否存在。
     *
     * @param username 用户名
     * @return 如果存在返回 true，否则返回 false
     */
    public boolean checkUsernameExists(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", username);
        // 使用 MyBatis Plus 的 exists 方法
        return userMapper.exists(queryWrapper);
    }

    /**
     * 根据用户ID查询用户信息。
     *
     * @param id 用户ID
     * @return 用户DTO
     */
    public Optional<UserDTO> getUserById(Long id) {
        User user = userMapper.selectById(id); // 使用 MyBatis Plus 的 selectById
        return Optional.ofNullable(user).map(this::convertToDto);
    }

    /**
     * 获取所有用户信息 (仅限管理员)。
     *
     * @return 用户DTO列表
     */
    public List<UserDTO> getAllUsers() {
        return userMapper.selectList(null).stream() // 使用 MyBatis Plus 的 selectList
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 更新用户信息。
     *
     * @param id      用户ID
     * @param userDTO 包含更新信息的DTO
     * @return 更新后的用户DTO
     * @throws IllegalArgumentException 如果用户不存在
     */
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userMapper.selectById(id); // 使用 MyBatis Plus 的 selectById
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }

        // 允许更新用户名和角色，但通常不直接更新密码（密码有单独的修改接口）
        if (userDTO.getUserName() != null && !userDTO.getUserName().equals(existingUser.getUserName())) {
            if (checkUsernameExists(userDTO.getUserName())) { // 使用新的 checkUsernameExists
                throw new IllegalArgumentException("New username already exists: " + userDTO.getUserName());
            }
            existingUser.setUserName(userDTO.getUserName());
        }

        // 角色更新（通常只有管理员能修改角色）
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            // 将 Set<String> 转换为逗号分隔的字符串
            existingUser.setRoles(String.join(",", userDTO.getRoles()));
        }


        userMapper.updateById(existingUser); // 使用 MyBatis Plus 的 updateById
        return convertToDto(existingUser); // 返回转换后的DTO
    }

    /**
     * 用户注销（通常是客户端删除 Token，服务端无状态）。
     * 如果需要黑名单机制，可以在此实现。
     *
     * @param token 待注销的 token
     */
    public void logout(String token) {
        // 对于无状态 JWT，注销通常意味着客户端删除其本地存储的 Token。
        // 如果需要服务器端强制注销（例如，将 Token 加入黑名单），可以在这里实现。
        // 例如：jwtTokenUtil.addToBlacklist(token);
        System.out.println("User logout (client-side token deletion assumed). Token: " + token);
    }

    /**
     * 将 User 实体转换为 UserDTO。
     */
    private UserDTO convertToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        // 将逗号分隔的角色字符串转换为 Set<String>
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setRoles(Arrays.stream(user.getRoles().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet()));
        } else {
            dto.setRoles(Collections.emptySet());
        }
        dto.setMobile(user.getMobile());
        dto.setEmail(user.getEmail());
        dto.setGender(user.getGender());
        dto.setAvatar(user.getAvatar());
        dto.setUserStatus(user.getUserStatus());

        // 不返回密码
        return dto;
    }
}

