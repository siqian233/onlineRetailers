package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("address")
@Data
@NoArgsConstructor
public class Address {
    @TableId("addressId")
    private Long addressId;

    @TableField("userId")
    private Long userId;

    @TableField("receiverName")
    private String receiverName ;

    @TableField("receiverMobile")
    private String receiverMobile ;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("district")
    private String district;

    @TableField("detailAddress")
    private String detailAddress;

    @TableField("isDefault")
    private Integer isDefault;

    @TableField("addressStatus")
    private Integer addressStatus;
}
