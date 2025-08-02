package com.lightning.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("Review")
@Data
@NoArgsConstructor
public class Review implements Serializable {
    private Long id ;// '评价ID',
    private Long productId ;//'商品ID',
    private Long userId ;//'用户ID',
    private Long orderId ;// '订单ID',
    private Integer rating ;// '评分(1-5星)',
    private String content ;// '评价内容',
    private String images ;// '评价图片(多个用逗号分隔)',
    private Integer isAnonymous;//'是否匿名(0-否 1-是)',
    private LocalDateTime createdAt ;//'评价时间',
    private Integer  reviewStatus; // '状态(0-被删除 1-正常)',
}
