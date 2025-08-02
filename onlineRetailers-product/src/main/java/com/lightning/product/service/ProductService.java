package com.lightning.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lightning.web.bean.ProductDTO;

public interface ProductService {

    /**
     * 分页查询商品
     * @param queryVO 查询参数，包含商品名称、分类ID、商品状态、排序方式等
     * @return 商品DTO的分页结果
     */
    IPage<ProductDTO> listProductsPaged(ProductQueryVO queryVO);

    /**
     * 查询特价商品列表
     * <p>特价商品定义为：当前价格price低于原价originalPrice，且商品状态为上架 (productStatus = 1),结果按商品ID降序排列</p>
     * @param queryVO 查询参数，包含排序方式等
     * @return 特价商品列表
     */
    IPage<ProductDTO> getSpecialOfferProducts(ProductQueryVO queryVO);

    /**
     * 上架商品 (新增商品)
     * @param productDTO 待上架的商品信息
     * @return 上架成功后的商品DTO
     */
    ProductDTO addProduct(ProductDTO productDTO);

    /**
     * 下架商品
     * @param productId 商品ID
     * @return 是否下架成功
     */
    boolean delistProduct(Long productId);

    /**
     * 更新商品信息
     * @param productDTO 待更新的商品信息
     * @return 更新成功后的商品DTO
     */
    ProductDTO updateProduct(ProductDTO productDTO);

    /**
     * 根据商品ID获取商品详情
     * @param productId 商品ID
     * @return 商品DTO
     */
    ProductDTO getProductById(Long productId);

    void addBloomFilter(Long bannerId);
}