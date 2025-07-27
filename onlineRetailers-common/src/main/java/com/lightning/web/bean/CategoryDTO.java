package com.lightning.web.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 商品分类数据传输对象 (Category Data Transfer Object)
 */
@Data
@NoArgsConstructor
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId; // 类别ID
    private String categoryName; // 分类名称
    private String icon; // 分类图标URL
    private String linkUrl; // 跳转链接
    private Integer categoryStatus; // 状态(0-被删除 1-正常)

    // 可以根据需要添加其他字段
    private MultipartFile iconFile; // 分类图标文件
}
