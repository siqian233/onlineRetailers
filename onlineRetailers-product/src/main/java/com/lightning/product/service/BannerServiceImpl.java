package com.lightning.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lightning.model.Banner;
import com.lightning.product.mapper.BannerMapper;
import com.lightning.web.api.FileUploadApi;
import com.lightning.web.api.IdGeneratorApi;
import com.lightning.web.bean.BannerDTO;
import com.lightning.web.bean.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log
public class BannerServiceImpl implements BannerService {

    public final static Integer BANNER_STATUS_ENABLE = 1;
    public final static Integer BANNER_STATUS_DISABLE = 0;
    private final BannerMapper bannerMapper;
    private final FileUploadApi fileUploadApi;
    private final IdGeneratorApi idGeneratorApi;

    @Override
    @Transactional
    public BannerDTO addBanner(BannerDTO bannerDTO) {
        ResponseResult rr = this.idGeneratorApi.getNextId();
        if (rr.getCode() != 1) {
            throw new RuntimeException("轮播图ID生成失败");
        }
        Long bannerId = Long.parseLong(rr.getData().toString());
        bannerDTO.setBannerId(bannerId);

        ResponseResult rr2 = this.fileUploadApi.upload(new MultipartFile[]{bannerDTO.getImageUrlFile()});
        if (rr2.getCode() != 1) {
            throw new RuntimeException("轮播图图片上传失败");
        }
        List<String> mainImages = (List<String>) rr2.getData();
        bannerDTO.setImageUrl(mainImages.get(0));

        if (bannerDTO.getBannerStatus() == null) {
            bannerDTO.setBannerStatus(BANNER_STATUS_ENABLE); // 默认正常
        }
        Banner banner = new Banner();
        BeanUtils.copyProperties(bannerDTO, banner);

        int result = this.bannerMapper.insert(banner);
        if (result > 0) {
            BeanUtils.copyProperties(banner, bannerDTO);

            return bannerDTO;
        } else {
            throw new RuntimeException("新增类别失败");
        }
    }

    @Override
    public List<BannerDTO> getBannersByStatus(Integer bannerStatus) {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        if (bannerStatus != null && bannerStatus != -1) {
            queryWrapper.eq(Banner::getBannerStatus, bannerStatus);
        }
        List<Banner> banners = this.bannerMapper.selectList(queryWrapper);

        return banners.stream()
                .map(banner -> {
                    BannerDTO dto = new BannerDTO();
                    BeanUtils.copyProperties(banner, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
