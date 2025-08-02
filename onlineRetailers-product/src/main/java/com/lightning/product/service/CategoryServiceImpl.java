package com.lightning.product.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lightning.model.Category;
import com.lightning.product.mapper.CategoryMapper;
import com.lightning.web.api.FileUploadApi;
import com.lightning.web.api.IdGeneratorApi;
import com.lightning.web.bean.CategoryDTO;
import com.lightning.web.bean.ResponseResult;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Log
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final RedissonClient redissonClient;
    private final String BLOOM_FILTER_KEY = "bloom:categories"; // 布隆过滤器的Redis Key

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private IdGeneratorApi idGeneratorApi;
    @Autowired
    private FileUploadApi fileUploadApi;

    /**
     * 新增类别
     *
     * @param categoryDTO 待新增的类别信息
     * @return 新增成功后的类别DTO
     */
    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        ResponseResult<Long> rr = this.idGeneratorApi.getNextId();
        if (rr.getCode() != 1) {
            throw new RuntimeException("类别ID生成失败");
        }
        Long categoryId = Long.parseLong(rr.getData().toString());
        categoryDTO.setCategoryId(categoryId);

        if (categoryDTO.getIconFile() != null) {
            uploadCategoryIcon(categoryDTO);
        }

        if (categoryDTO.getCategoryStatus() == null) {
            categoryDTO.setCategoryStatus(1); // 默认正常
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        int result = categoryMapper.insert(category);
        if (result <= 0) {
            throw new RuntimeException("新增类别失败");
        }

        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        if (!bloomFilter.isExists()) {
            bloomFilter.tryInit(10000L, 0.01);// 初始化：预计元素10000，误判率为0.01
        }
        bloomFilter.add(categoryDTO.getCategoryId());

        BeanUtils.copyProperties(category, categoryDTO);

        return categoryDTO;
    }

    /**
     * 查询所有类别
     *
     * @return 所有类别DTO的列表
     */
    @Override
    @Cacheable(value = "categories", key = "#categoryStatus != null ? 'status_' + #categoryStatus : 'all'")
    public List<CategoryDTO> getAllCategories(Integer categoryStatus) {
        // 查询所有未被删除的类别 (categoryStatus = 1)
        LambdaUpdateWrapper<Category> queryWrapper = new LambdaUpdateWrapper<>();
        if (categoryStatus != null) {
            queryWrapper.eq(Category::getCategoryStatus, categoryStatus); // 只查询状态为正常的类别
        }
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        // 将实体列表转换为DTO列表
        return categories.stream()
                .map(category -> {
                    CategoryDTO dto = new CategoryDTO();
                    BeanUtils.copyProperties(category, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 修改类别状态
     *
     * @param categoryId 类别ID
     * @return 是否设置成功
     */
    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public boolean setCategoryStatusToHidden(Long categoryId, Integer categoryStatus) {
        // 构建更新条件，只更新状态为0 (被删除/隐藏)
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Category::getCategoryId, categoryId)
                .set(Category::getCategoryStatus, categoryStatus); // 0-被删除/隐藏

        int result = categoryMapper.update(null, updateWrapper);
        return result > 0;
    }

    /**
     * 更新类别信息
     *
     * @param categoryDTO 待更新的类别信息
     * @return 更新成功后的类别DTO
     */
    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getCategoryId() == null) {
            throw new IllegalArgumentException("类别ID不能为空，无法更新");
        }

        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        if (bloomFilter.contains(categoryDTO.getCategoryId())) {
            log.warning("布隆过滤器拦截：categoryId 不存在 -> " + categoryDTO.getCategoryId());
            return null;
        }

        // 图标上传并设置URL (如果图标数据有变化)
        if (categoryDTO.getIconFile() != null) {
            uploadCategoryIcon(categoryDTO);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        int result = categoryMapper.updateById(category);
        if (result <= 0) {
            throw new RuntimeException("类别更新失败，类别ID: " + categoryDTO.getCategoryId());
        }

        return getCategoryById(categoryDTO.getCategoryId());
    }

    /**
     * 上传类别图标
     *
     * @param categoryDTO 待上传的类别信息
     */
    private void uploadCategoryIcon(CategoryDTO categoryDTO) {
        ResponseResult<List<String>> rr2 = this.fileUploadApi.upload(new MultipartFile[]{categoryDTO.getIconFile()});
        if (rr2.getCode() != 1) {
            throw new RuntimeException("类别主图片上传失败");
        }
        List<String> mainImages = rr2.getData();
        categoryDTO.setIcon(mainImages.get(0));
    }

    /**
     * 根据ID获取类别详情
     *
     * @param categoryId 类别ID
     * @return 类别DTO
     */
    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_KEY);
        if (bloomFilter.contains(categoryId)) {
            log.warning("布隆过滤器拦截：categoryId 不存在 -> " + categoryId);
            return null;
        }

        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(category, dto);

        return dto;
    }
}

