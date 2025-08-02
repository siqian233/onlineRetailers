package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("Orders")
@Data
@NoArgsConstructor
public class Orders implements Serializable {
    @TableId("id")
    private Long id ;// '订单id',

    @TableField("userId")
    private Long userId ;// '用户id',
    @TableField("addressId")
    private Long addressId ;//'收货地址id',
    @TableField("paymentType")
    private Integer  paymentType ;//'支付方式(1-支付宝 2-微信 3-银行卡)',
    @TableField("paymentTime")
    private LocalDateTime paymentTime ;// '支付时间',
    @TableField("orderStatus")
    private Integer orderStatus ; // '状态(0-订单取消 1-待支付 2-已支付待发货 3-已发货 4-已签收 5-订单完成 6-申请售后 7-退货退款)',
    @TableField("shippingTime")
    private LocalDateTime shippingTime ; // '发货时间',
    @TableField("updateTime")
    private LocalDateTime updateTime ; // '最新操作时间',
    @TableField("remark")
    private String remark ; // '备注'
}
