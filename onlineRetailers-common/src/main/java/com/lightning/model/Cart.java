package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("cart")
@Data
@NoArgsConstructor
public class Cart implements Serializable {
    @TableId("cartId")
    private Long cartId ;// '购物车ID',
    @TableField("userId")
    private Long userId ;// '用户ID',
    @TableField("productId")
    private Long productId ;// '商品ID',
    @TableField("quantity")
    private Integer  quantity ;// '数量',
}
