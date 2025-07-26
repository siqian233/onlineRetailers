package com.lightning.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lightning.model.Product;
import com.lightning.product.mapper.ProductMapper;
import com.lightning.web.api.FileUploadApi;
import com.lightning.web.api.IdGeneratorApi;
import com.lightning.web.bean.ProductDTO;
import com.lightning.web.bean.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IdGeneratorApi idGeneratorApi;
    @Autowired
    private FileUploadApi fileUploadApi;

    /**
     * 分页查询商品
     *
     * @param queryVO 查询参数，包含商品名称、分类ID、商品状态、排序方式等
     * @return 商品DTO的分页结果
     */
    @Override
    public IPage<ProductDTO> listProductsPaged(ProductQueryVO queryVO) {
        Page<Product> page = new Page<>(queryVO.getCurrent(), queryVO.getSize());

        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();

        // 根据商品名称模糊查询
        if (StringUtils.hasText(queryVO.getProductName())) {
            queryWrapper.like(Product::getProductName, queryVO.getProductName());
        }
        // 根据分类ID精确查询
        if (queryVO.getCategoryId() != null) {
            queryWrapper.eq(Product::getCategoryId, queryVO.getCategoryId());
        }
        // 根据商品状态查询
        if (queryVO.getProductStatus() != null) {
            queryWrapper.eq(Product::getProductStatus, queryVO.getProductStatus());
        }

        // 处理排序
        if (StringUtils.hasText(queryVO.getSortField())) {
            boolean isAsc = !"desc".equalsIgnoreCase(queryVO.getSortOrder());
            switch (queryVO.getSortField().toLowerCase()) {
                case "price":
                    queryWrapper.orderBy(true, isAsc, Product::getPrice);
                    break;
                case "productid":
                    queryWrapper.orderBy(true, isAsc, Product::getProductId);
                    break;
                default:
                    queryWrapper.orderByDesc(Product::getProductStatus);
                    break;
            }
        } else {
            // 默认排序
            queryWrapper.orderByDesc(Product::getProductId);
        }
        // 执行分页查询
        IPage<Product> productPage = productMapper.selectPage(page, queryWrapper);
        // 将实体分页结果转换为DTO分页结果
        return productPage.convert(product -> {
            ProductDTO dto = new ProductDTO();
            BeanUtils.copyProperties(product, dto);
            // 这里可以添加从其他服务获取分类名称的逻辑，例如通过Feign调用分类服务
            // dto.setCategoryName(categoryService.getCategoryNameById(product.getCategoryId()));
            return dto;
        });
    }

    /**
     * 上架商品 (新增商品)
     *
     * @param productDTO 待上架的商品信息
     * @return 上架成功后的商品DTO
     */
    @Override
    @Transactional // 开启事务
    public ProductDTO addProduct(ProductDTO productDTO) {
        ResponseResult rr = this.idGeneratorApi.getNextId();
        if (rr.getCode() != 1) {
            throw new RuntimeException("商品ID生成失败");
        }
        Long productId = Long.parseLong(rr.getData().toString());
        productDTO.setProductId(productId);

        ResponseResult rr2 = this.fileUploadApi.upload(new MultipartFile[]{productDTO.getMainImageFile()});
        if (rr2.getCode() != 1) {
            throw new RuntimeException("商品主图片上传失败");
        }
        List<String> mainImages = (List<String>) rr2.getData();
        productDTO.setMainImage(mainImages.get(0));

        MultipartFile[] subImageFiles = productDTO.getSubImageFiles();
        ResponseResult rr3 = this.fileUploadApi.upload(subImageFiles);
        List<String> subImages = (List<String>) rr3.getData();
        StringBuilder uploadedSubImages = new StringBuilder();
        for (String imgData : subImages) {
            if (StringUtils.hasText(imgData)) {
                uploadedSubImages.append(imgData).append(",");
            }
        }
        if (uploadedSubImages.length() > 0) {
            uploadedSubImages.setLength(uploadedSubImages.length() - 1); // 移除最后一个逗号
        }
        productDTO.setSubImages(uploadedSubImages.toString());

        if (productDTO.getProductStatus() == null) {
            productDTO.setProductStatus(1); // 默认上架
        }

        if (productDTO.getPrice() == null) {
            productDTO.setPrice(BigDecimal.ZERO);
        }

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        int result = productMapper.insert(product);
        if (result > 0) {
            BeanUtils.copyProperties(product, productDTO);
            return productDTO;
        } else {
            throw new RuntimeException("商品上架失败");
        }
    }

    /**
     * 下架商品
     *
     * @param productId 商品ID
     * @return 是否下架成功
     */
    @Override
    @Transactional
    public boolean delistProduct(Long productId) {
        // 构建更新条件，只更新状态
        LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Product::getProductId, productId)
                .set(Product::getProductStatus, 0); // 0-下架

        int result = productMapper.update(null, updateWrapper);
        return result > 0;
    }

    /**
     * 更新商品信息
     *
     * @param productDTO 待更新的商品信息
     * @return 更新成功后的商品DTO
     */
    @Override
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO) {
        if (productDTO.getProductId() == null) {
            throw new IllegalArgumentException("商品ID不能为空，无法更新");
        }

        if (productDTO.getMainImageFile() != null) {
            ResponseResult rr2 = this.fileUploadApi.upload(new MultipartFile[]{productDTO.getMainImageFile()});
            if (rr2.getCode() != 1) {
                throw new RuntimeException("商品主图片上传失败");
            }
            List<String> mainImages = (List<String>) rr2.getData();
            productDTO.setMainImage(mainImages.get(0));
        }

        if (productDTO.getSubImageFiles() != null && productDTO.getSubImageFiles().length > 0) {
            // 假设subImages是逗号分隔的图片数据，这里需要循环上传
            MultipartFile[] subImageFiles = productDTO.getSubImageFiles();
            ResponseResult rr3 = this.fileUploadApi.upload(subImageFiles);
            List<String> subImages = (List<String>) rr3.getData();
            StringBuilder uploadedSubImages = new StringBuilder();
            for (String imgData : subImages) {
                if (StringUtils.hasText(imgData)) {
                    uploadedSubImages.append(imgData).append(",");
                }
            }
            if (uploadedSubImages.length() > 0) {
                uploadedSubImages.setLength(uploadedSubImages.length() - 1); // 移除最后一个逗号
            }
            productDTO.setSubImages(uploadedSubImages.toString());
        }

        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        // 执行更新，MyBatis Plus会根据实体中的@TableId注解自动识别主键
        int result = productMapper.updateById(product);
        if (result > 0) {
            // 更新成功后，返回最新的商品信息
            return getProductById(productDTO.getProductId());
        } else {
            throw new RuntimeException("商品更新失败，商品ID: " + productDTO.getProductId());
        }
    }

    /**
     * 根据商品ID获取商品详情
     *
     * @param productId 商品ID
     * @return 商品DTO
     */
    @Override
    public ProductDTO getProductById(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }
}
