package com.lightning.product.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lightning.product.service.ProductQueryVO;
import com.lightning.product.service.ProductService;
import com.lightning.web.bean.ProductDTO;
import com.lightning.web.bean.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 商品API控制器
 */
@RestController
@RequestMapping("/product/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 上架商品 (新增商品)
     * POST /products
     *
     * @param productDTO 待上架的商品信息
     * @return 包含上架成功后商品DTO的统一响应
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseResult<ProductDTO>> addProduct(ProductDTO productDTO) {
        try {
            ProductDTO addedProduct = productService.addProduct(productDTO);
            ResponseResult<ProductDTO> result= ResponseResult.ok("商品上架成功");

            // 商品创建成功，返回HTTP 201 Created，业务码1，并携带新增商品数据
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result.setData(addedProduct));
        } catch (Exception e) {

            // 捕获异常，返回HTTP 500 Internal Server Error，业务码0，并提示错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseResult.error("商品上架失败: " + e.getMessage()));
        }
    }


    /**
     * 分页查询商品列表
     * GET /products/page
     * 参数示例: /products/page?current=1&size=10&productName=手机&categoryId=1&productStatus=1&sortField=price&sortOrder=desc
     *
     * @param queryVO 查询参数
     * @return 包含商品DTO分页结果的统一响应
     */
    @GetMapping("/page")
    public ResponseEntity<ResponseResult<IPage<ProductDTO>>> getProductsPaged(ProductQueryVO queryVO) {
        IPage<ProductDTO> productPage = productService.listProductsPaged(queryVO);
        ResponseResult<IPage<ProductDTO> > result= ResponseResult.ok("查询成功");

        return ResponseEntity.ok(result.setData(productPage));
    }

    /**
     * 获取特价商品列表
     * @return 包含特价商品列表的ResponseEntity
     */
    @GetMapping("/special-offers")
    public ResponseEntity<ResponseResult<IPage<ProductDTO>>> getSpecialOfferProducts( ProductQueryVO queryVO) {
        IPage<ProductDTO> specialOfferProducts = productService.getSpecialOfferProducts(queryVO);
        ResponseResult<IPage<ProductDTO> > result= ResponseResult.ok("查询成功");

        // 即使列表为空，也认为成功，返回空列表
        return ResponseEntity.ok(result.setData(specialOfferProducts));
    }

    /**
     * 根据ID获取商品详情
     * GET /products/{productId}
     *
     * @param productId 商品ID
     * @return 包含商品DTO的统一响应
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ResponseResult<ProductDTO>> getProductById(@PathVariable("productId") Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        if (productDTO != null) {
            // 找到商品，返回HTTP 200 OK，业务码1，并携带商品数据
            ResponseResult<ProductDTO> result= ResponseResult.ok("查询成功");
            return ResponseEntity.ok(result.setData(productDTO));
        } else {
            // 未找到商品，返回HTTP 404 Not Found，业务码0，并提示错误信息
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseResult.error("商品不存在，ID: " + productId));
        }
    }


    /**
     * 下架商品 (更新商品状态)
     * PUT /products/{productId}/delist
     *
     * @param productId 商品ID
     * @return 操作结果的统一响应
     */
    @PutMapping("/{productId}/delist")
    public ResponseEntity<ResponseResult<Void>> delistProduct(@PathVariable("productId") Long productId) {
        boolean success = productService.delistProduct(productId);
        if (success) {
            // 下架成功，返回HTTP 200 OK，业务码1
            // 也可以选择返回 204 No Content，但为了统一响应体，返回200 OK
            return ResponseEntity.ok(ResponseResult.ok("商品下架成功"));
        } else {
            // 下架失败（例如商品不存在），返回HTTP 404 Not Found，业务码0
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseResult.error("商品下架失败，商品ID不存在或已下架: " + productId));
        }
    }

    /**
     * 更新商品信息
     * PUT /products/{productId}
     *
     * @param productId  商品ID
     * @param productDTO 待更新的商品信息
     * @return 包含更新成功后商品DTO的统一响应
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ResponseResult<ProductDTO>> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDTO productDTO) {
        if (!productId.equals(productDTO.getProductId())) {
            // 请求路径ID与DTO中的ID不一致，返回HTTP 400 Bad Request，业务码0
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseResult.error("请求路径中的商品ID与请求体中的商品ID不一致"));
        }
        try {
            ProductDTO updatedProduct = productService.updateProduct(productDTO);
            ResponseResult<ProductDTO> result= ResponseResult.ok("商品更新成功");
            // 更新成功，返回HTTP 200 OK，业务码1，并携带更新后的商品数据
            return ResponseEntity.ok(result.setData(updatedProduct));
        } catch (IllegalArgumentException e) {
            // 参数非法（例如商品ID为空），返回HTTP 400 Bad Request，业务码0
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseResult.error("请求参数错误: " + e.getMessage()));
        } catch (Exception e) {
            // 捕获其他异常，返回HTTP 500 Internal Server Error，业务码0
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseResult.error("商品更新失败: " + e.getMessage()));
        }
    }

    /**
     * 测试布隆过滤器
     * POST /products/test/bloom/add
     *
     * @param productId 商品ID
     * @return 布隆过滤器添加结果
     */
    @PostMapping("/test/bloom/add")
    public ResponseEntity<ResponseResult<Void>> addBloomFilter(@RequestParam("productId") Long productId) {
        productService.addBloomFilter(productId);

        return ResponseEntity.ok(ResponseResult.ok("添加布隆过滤成功"));
    }
}