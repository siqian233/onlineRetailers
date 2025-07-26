package com.lightning.product.service;

import com.lightning.web.bean.BannerDTO;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log
public class BannerServiceImpl implements BannerService{

    @Override
    public BannerDTO addBanner(BannerDTO banner) {
        return null;
    }

    @Override
    public List<BannerDTO> getBannerByStatus(Integer status) {
        return List.of();
    }
}
