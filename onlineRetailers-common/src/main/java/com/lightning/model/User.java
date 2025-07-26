package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("user")
@Data
@NoArgsConstructor
public class User {
    @TableId("userId")
    private Long userId;

    @TableField("userName") // 映射到 username 字段
    private String userName;

    @TableField("userPassword") // 映射到 password 字段
    private String userPassword; // 存储的是加密后的密码

    @TableField("mobile") // 映射到 mobile 字段
    private String mobile; // 手机号

    @TableField("email") // 映射到 email 字段
    private String email; // 邮箱

    @TableField("avatar") // 映射到 avatar 字段
    private String avatar; // 头像 URL

    @TableField("gender") // 映射到 gender 字段
    private Integer gender; // 性别 (0-未知, 1-男, 2-女)

    @TableField("userStatus") // 映射到 user_status 字段
    private Integer userStatus; // 用户状态 (0-禁用, 1-正常)

    @TableField("roles") // 映射到 roles 字段，存储逗号分隔的角色字符串
    private String roles; // 用户的角色，例如 "ADMIN,CUST"
}
