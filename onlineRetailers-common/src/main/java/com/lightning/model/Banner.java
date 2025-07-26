package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("banner")
@Data
@NoArgsConstructor
public class Banner {
    @TableId("bannerId")
    private Long bannerId; //  '轮播图ID',
    @TableField("imageUrl")
    private String imageUrl; // '图片URL',
    @TableField("linkUrl")
    private String linkUrl; //  '跳转链接',
    @TableField("title")
    private String title; // '标题',
    @TableField("bannerStatus")
    private Integer bannerStatus;  //  '状态(0-禁用 1-启用)',
}
