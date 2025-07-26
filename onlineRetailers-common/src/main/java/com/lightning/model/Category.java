package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("Category")
@Data
@NoArgsConstructor
public class Category {
    @TableId("categoryId")
    private Long categoryId;

    @TableField("categoryName")
    private String categoryName;

    @TableField("icon")
    private String icon;

    @TableField("linkUrl")
    private String linkUrl;

    @TableField("categoryStatus")
    private Integer categoryStatus;
}
