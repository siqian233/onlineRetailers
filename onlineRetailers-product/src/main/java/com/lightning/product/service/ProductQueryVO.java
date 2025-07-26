package com.lightning.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品查询视图对象，用于封装分页和查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true) // 继承Page时需要
public class ProductQueryVO extends Page implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productName; // 商品名称
    private Long categoryId; // 分类ID
    private Integer productStatus; // 商品状态 (0-下架 1-上架)

    // 排序字段，例如 "price", "productCode"
    private String sortField;
    // 排序顺序，例如 "asc", "desc"
    private String sortOrder;

    public ProductQueryVO() {
        // 默认分页参数
        super(1, 10); // 默认第一页，每页10条
    }
}
