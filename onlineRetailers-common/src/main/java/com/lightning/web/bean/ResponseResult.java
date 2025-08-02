package com.lightning.web.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult <T>{
    private int code;
    private String msg;
    private T data;

    public static <T> ResponseResult<T> ok(){
        return ResponseResult.<T>builder().code(1).msg("操作成功").build();
    }
    public static <T> ResponseResult<T> ok(String msg){
        return ResponseResult.<T>builder().code(1).msg(msg).build();
    }

    public static <T> ResponseResult<T> error(){
        return ResponseResult.<T>builder().code(0).msg("操作失败").build();
    }
    public static <T> ResponseResult<T> error(String msg){
        return ResponseResult.<T>builder().code(0).msg(msg).build();
    }

    public static <T> ResponseResult<T> noLogin(){
        return ResponseResult.<T>builder().code(-1).msg("请先登录").build();
    }

    public ResponseResult <T> setData(T t){
        this.data = t;
        return this;
    }
}
