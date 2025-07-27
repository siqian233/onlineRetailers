package com.lightning.web.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品数据传输对象 (Product Data Transfer Object)
 */
@Data
@NoArgsConstructor
public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long productId; // 商品ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId; // 分类ID

    private String productDesc; // 商品描述

    private String mainImage; // 主图URL
    private MultipartFile mainImageFile; // 主图文件

    private String subImages; // 子图URL(多个图片用逗号分隔)
    private MultipartFile[] subImageFiles; // 子图文件

    private String detail; // 商品详情
    private BigDecimal price; // 当前价格
    private BigDecimal originalPrice; // 原价
    private Integer productStatus; // 状态(0-下架 1-上架)

    // 可以添加其他DTO特有的字段，例如分类名称等
    private String categoryName;
}

