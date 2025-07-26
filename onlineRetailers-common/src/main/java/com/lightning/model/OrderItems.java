package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("Order_items")
@Data
@NoArgsConstructor
public class OrderItems {
    @TableId("id")
    private Long id;// '订单详情id',
    @TableField("orderId")
    private Long orderId;// '订单id',
    @TableField("productId")
    private Long productId;// '商品id',
    @TableField("quantity")
    private Integer quantity;// '数量',
    @TableField("price")
    private Double price;// '商品单价',
}
