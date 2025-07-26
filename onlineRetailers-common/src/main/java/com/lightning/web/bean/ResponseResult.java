package com.lightning.web.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder       //构造器注解，声明后，该类对象不用new来实例化对象，并且可以链式编程。
@NoArgsConstructor // 生成无参构造器
@AllArgsConstructor // 生成全参构造器（供 @Builder 使用）
public class ResponseResult {
    private int code;
    private String msg;
    private Object data;

    public static ResponseResult ok(){
        return ResponseResult.builder().code(1).msg("操作成功").build();
    }
    public static ResponseResult ok(String msg){
        return ResponseResult.builder().code(1).msg(msg).build();
    }

    public static ResponseResult error(){
        return ResponseResult.builder().code(0).msg("操作失败").build();
    }
    public static ResponseResult error(String msg){
        return ResponseResult.builder().code(0).msg(msg).build();
    }

    public static ResponseResult noLogin(){
        return ResponseResult.builder().code(-1).msg("请先登录").build();
    }

    public <T> ResponseResult setData(T t){
        this.data = t;
        return this;
    }
}
