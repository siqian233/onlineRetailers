package com.lightning.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lightning.web.bean.ProductDTO;

/**
 * @program: shopping147_cloud
 * @description:
 * @author: zy
 * @create: 2025-07-24 19:31
 */
public interface ProductService {

    /**
     * 分页查询商品
     * @param queryVO 查询参数，包含商品名称、分类ID、商品状态、排序方式等
     * @return 商品DTO的分页结果
     */
    IPage<ProductDTO> listProductsPaged(ProductQueryVO queryVO);

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

}


