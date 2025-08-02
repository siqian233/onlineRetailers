package com.lightning.product.web.controller;

import com.lightning.product.service.BannerService;
import com.lightning.web.bean.BannerDTO;
import com.lightning.web.bean.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图API控制器
 **/
@RestController
@RequestMapping("/product/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 新增轮播图
     * POST /banner/add
     * @param bannerDTO 待新增的类别信息
     * @return 包含新增成功后轮播图DTO的统一响应
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseResult<BannerDTO>> addBanner(BannerDTO bannerDTO) {
        try {
            BannerDTO addedBanner = bannerService.addBanner(bannerDTO);
            addedBanner.setImageUrlFile(null);
            ResponseResult<BannerDTO> result = ResponseResult.ok("轮播图新增成功");
            // 类别创建成功，返回HTTP 201 Created，业务码1，并携带新增类别数据
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result.setData(addedBanner));
        } catch (Exception e) {
            // 捕获异常，返回HTTP 500 Internal Server Error，业务码0，并提示错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseResult.error("轮播图新增失败: " + e.getMessage()));
        }
    }

    /**
     * 根据状态查询轮播图
     * GET /banner/status/{bannerStatus}
     * @param bannerStatus 轮播图状态: 要求三个值: -1所有, 0隐藏, 1显示
     * @return 包含轮播图DTO列表的统一响应
     */
    @GetMapping("/status/{bannerStatus}")
    public ResponseEntity<ResponseResult<List<BannerDTO>>> getBannersByStatus(
            @PathVariable( value="bannerStatus") Integer bannerStatus) {
        List<BannerDTO> banners = bannerService.getBannersByStatus(bannerStatus);
        ResponseResult<List<BannerDTO>> result = ResponseResult.ok("查询轮播图成功");

        return ResponseEntity.ok(result.setData(banners));
    }

    /**
     * 根据轮播图ID查询轮播图
     * @param bannerId 轮播图ID
     * @return 轮播图DTO
     */
    @GetMapping("/{bannerId}")
    public ResponseEntity<ResponseResult<BannerDTO>> getBannerById(@PathVariable("bannerId") Long bannerId) {
        BannerDTO banner = bannerService.getBannerById(bannerId);
        ResponseResult<BannerDTO> result = ResponseResult.ok("查询轮播图成功");
        return ResponseEntity.ok(result.setData(banner));
    }

    /**
     * 测试用-添加布隆过滤器
     * @param bannerId 轮播图ID
     * @return 布隆过滤器添加结果
     */
    @PostMapping("/test/bloom/add")
    public ResponseEntity<ResponseResult<Void>> addBloomFilter(@RequestParam("bannerId") Long bannerId) {
        bannerService.addBloomFilter(bannerId);
        return ResponseEntity.ok(ResponseResult.ok("添加布隆过滤成功"));
    }

}