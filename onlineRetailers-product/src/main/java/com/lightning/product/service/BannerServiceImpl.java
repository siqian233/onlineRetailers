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
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
@AllArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final RedissonClient redissonClient;
    private final String BLOOM_FILTER_KEY = "bloom:banners"; // 布隆过滤器的Redis Key
    public final static Integer BANNER_STATUS_ENABLE = 1;
    public final static Integer BANNER_STATUS_DISABLE = 0;
    private final BannerMapper bannerMapper;
    private final FileUploadApi fileUploadApi;
    private final IdGeneratorApi idGeneratorApi;

    /**
     * 新增轮播图
     * @param bannerDTO 轮播图数据
     * @return 新增的轮播图数据
     */
    @Override
    @Transactional
    @CacheEvict(value = "banners", allEntries = true)
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
        if (result <= 0) {
            throw new RuntimeException("新增类别失败");
        }

        addBloomFilterElement(bannerDTO.getBannerId());

        addBloomFilterElement(bannerId);

        return bannerDTO;
    }

    /**
     * 根据状态查询轮播图
     * @param bannerStatus 轮播图状态
     * @return 轮播图列表
     */
    @Override
    @Cacheable(value = "banners", key = "#bannerStatus != null ? 'status_' + #bannerStatus : 'all'")
    public List<BannerDTO> getBannersByStatus(Integer bannerStatus) {

        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        if (bannerStatus != null && bannerStatus != -1) {
            queryWrapper.eq(Banner::getBannerStatus, bannerStatus); // 根据状态查询
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

    /**
     * 根据ID查询轮播图
     * @param bannerId 轮播图ID
     * @return 轮播图数据
     */
    @Override
    public BannerDTO getBannerById(Long bannerId) {
        if (!isBloomFilterContains(bannerId)) {
            log.warning("布隆过滤器拦截：bannerId 不存在 -> " + bannerId);
            return null;
        }

        Banner banner = this.bannerMapper.selectById(bannerId);
        if (banner == null) {
            return null;
        }

        BannerDTO dto = new BannerDTO();
        BeanUtils.copyProperties(banner, dto);

        return dto;
    }

    @Override
    public boolean addBloomFilter(Long bannerId){
        return addBloomFilterElement(bannerId);
    }

    public boolean isBloomFilterContains(Long bannerId) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        return bloomFilter.contains(bannerId);
    }

    private boolean addBloomFilterElement(Long bannerId) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        if (!bloomFilter.isExists()) {
            bloomFilter.tryInit(10000L, 0.01);
        }
       return bloomFilter.add(bannerId);
    }
}
