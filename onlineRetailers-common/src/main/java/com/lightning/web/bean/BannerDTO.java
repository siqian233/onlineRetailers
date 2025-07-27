package com.lightning.web.bean;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class BannerDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long bannerId ;  //  '轮播图ID',
    private String imageUrl ;// '图片URL',
    private String linkUrl ;//  '跳转链接',
    private String title ;// '标题',
    private Integer bannerStatus;

    private MultipartFile imageUrlFile;

}