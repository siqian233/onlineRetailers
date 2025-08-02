package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@TableName("Product")
@Data
@NoArgsConstructor
public class Product implements Serializable {
    @TableId("productId")
    private Long productId;//'商品ID',
    @TableField("categoryId")
    private Long categoryId;// '分类ID',
    @TableField("productName")
    private String productName;// '商品名称',
    @TableField(exist = false)
    private String productCode;// '商品编码',
    @TableField("mainImage")
    private String mainImage;// '主图URL',
    @TableField("subImages")
    private String subImages;// '子图URL(多个图片用逗号分隔)',
    @TableField("detail")
    private String detail;//'商品详情',
    @TableField("price")
    private BigDecimal price;// '当前价格',
    @TableField("originalPrice")
    private BigDecimal originalPrice;//'原价',
    @TableField("productStatus")
    private Integer productStatus;  //  '状态(0-下架 1-上架)',
}
