package com.lightning.product.service;

import com.lightning.web.bean.BannerDTO;

import java.util.List;

public interface BannerService {
    BannerDTO addBanner(BannerDTO banner);
    List<BannerDTO> getBannersByStatus(Integer status);
    BannerDTO getBannerById(Long bannerId);
    boolean addBloomFilter(Long bannerId);
}
