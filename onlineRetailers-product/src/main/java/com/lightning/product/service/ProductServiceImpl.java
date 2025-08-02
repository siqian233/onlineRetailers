package com.lightning.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lightning.model.Product;
import com.lightning.product.mapper.ProductMapper;
import com.lightning.web.api.FileUploadApi;
import com.lightning.web.api.IdGeneratorApi;
import com.lightning.web.bean.ProductDTO;
import com.lightning.web.bean.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Log
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final RedissonClient redissonClient;
    private final String BLOOM_FILTER_KEY = "bloom:product";

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
     * 查询特价商品列表的实现
     * 特价商品定义为：当前价格低于原价，且商品状态为上架 (product_status = 1)
     * 结果按商品ID降序排列
     *
     * @return 特价商品列表
     */
    @Override
    public IPage<ProductDTO> getSpecialOfferProducts(ProductQueryVO queryVO) {
        // 创建分页对象
        Page<Product> page = new Page<>(queryVO.getCurrent(), queryVO.getSize());
        // 使用MyBatis-Plus的QueryWrapper构建查询条件
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        // 注意：这里的"price"和"original_price"是数据库列名
        // 修正：使用 apply 方法进行列与列之间的比较，避免将列名作为参数
        queryWrapper.apply("price < originalprice") //    where price < original_price
                .eq("productstatus", 1)      // product_status = 1
                .orderByDesc("productid");   // order by product_id DESC

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
        // 生成商品ID
        Long productId = generateProductId();
        productDTO.setProductId(productId);

        // 处理主图上传
        processMainImage(productDTO);

        // 处理副图上传
        processSubImages(productDTO);

        // 设置默认值
        setDefaultValues(productDTO);

        // 保存商品
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);

        int result = productMapper.insert(product);
        if (result <= 0) {
            throw new RuntimeException("商品上架失败");
        }

        addBloomFilterElement(productId);

        BeanUtils.copyProperties(product, productDTO);

        return productDTO;
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
        if (productId == null || isBloomFilterContains(productId)) {
            throw new IllegalArgumentException("商品ID为空或不存在，无法下架");
        }

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
        if (productDTO.getProductId() == null || isBloomFilterContains(productDTO.getProductId())) {
            throw new IllegalArgumentException("商品ID为空或不存在，无法更新");
        }

        // 处理主图上传（如果有新图片）
        if (productDTO.getMainImageFile() != null) {
            processMainImage(productDTO);
        }

        // 处理副图上传（如果有新图片）
        if (productDTO.getSubImageFiles() != null && productDTO.getSubImageFiles().length > 0) {
            processSubImages(productDTO);
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
        if (isBloomFilterContains(productId)) {
            log.warning("布隆过滤器拦截：productId 不存在 -> " + productId);
            return null;
        }

        Product product = productMapper.selectById(productId);
        if (product == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);

        return dto;
    }

    @Override
    public void addBloomFilter(Long productId){
        addBloomFilterElement(productId);
    }

    public boolean isBloomFilterContains(Long productId) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        return !bloomFilter.contains(productId);
    }

    private void addBloomFilterElement(Long productId) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        if (!bloomFilter.isExists()) {
            bloomFilter.tryInit(10000L, 0.01);
        }
        bloomFilter.add(productId);
    }

    /**
     * 生成商品ID
     *
     * @return 生成的商品ID
     */
    private Long generateProductId() {
        ResponseResult<Long> rr = this.idGeneratorApi.getNextId();
        if (rr.getCode() != 1) {
            throw new RuntimeException("商品ID生成失败");
        }
        return rr.getData();
    }

    /**
     * 处理主图上传
     *
     * @param productDTO 商品DTO
     */
    private void processMainImage(ProductDTO productDTO) {
        ResponseResult<List<String>> rr = this.fileUploadApi.upload(new MultipartFile[]{productDTO.getMainImageFile()});
        if (rr.getCode() != 1) {
            throw new RuntimeException("商品主图片上传失败");
        }
        List<String> mainImages = rr.getData();
        productDTO.setMainImage(mainImages.get(0));
    }

    /**
     * 处理副图上传
     *
     * @param productDTO 商品DTO
     */
    private void processSubImages(ProductDTO productDTO) {
        ResponseResult<List<String>> rr = this.fileUploadApi.upload(productDTO.getSubImageFiles());
        List<String> subImages = rr.getData();
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

    /**
     * 设置商品默认值
     *
     * @param productDTO 商品DTO
     */
    private void setDefaultValues(ProductDTO productDTO) {
        if (productDTO.getProductStatus() == null) {
            productDTO.setProductStatus(1); // 默认上架
        }

        if (productDTO.getPrice() == null) {
            productDTO.setPrice(BigDecimal.ZERO);
        }
    }
}
