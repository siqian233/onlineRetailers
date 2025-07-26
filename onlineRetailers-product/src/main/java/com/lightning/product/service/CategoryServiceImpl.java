package com.lightning.product.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lightning.model.Category;
import com.lightning.product.mapper.CategoryMapper;
import com.lightning.web.api.FileUploadApi;
import com.lightning.web.api.IdGeneratorApi;
import com.lightning.web.bean.CategoryDTO;
import com.lightning.web.bean.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

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
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        // 1. 生成类别ID
        ResponseResult rr = this.idGeneratorApi.getNextId();
        if (rr.getCode() != 1) {
            throw new RuntimeException("类别ID生成失败");
        }
        Long categoryId = Long.parseLong(rr.getData().toString());
        categoryDTO.setCategoryId(categoryId);

        // 2. 模拟图标上传并设置URL
        ResponseResult rr2 = this.fileUploadApi.upload(new MultipartFile[]{categoryDTO.getIconFile()});
        if (rr2.getCode() != 1) {
            throw new RuntimeException("类别主图片上传失败");
        }
        List<String> mainImages = (List<String>) rr2.getData();
        categoryDTO.setIcon(mainImages.get(0));
        // 3. 设置默认状态为正常
        if (categoryDTO.getCategoryStatus() == null) {
            categoryDTO.setCategoryStatus(1); // 默认正常
        }
        // 4. 将DTO转换为实体
        Category category = new Category();
        // spring 提供一个工具类，用于完成 对象的属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);
        // 5. 插入数据库
        int result = categoryMapper.insert(category);
        if (result > 0) {
            // 插入成功后，返回包含完整信息的DTO
            BeanUtils.copyProperties(category, categoryDTO);
            return categoryDTO;
        } else {
            throw new RuntimeException("新增类别失败");
        }
    }

    /**
     * 查询所有类别
     *
     * @return 所有类别DTO的列表
     */
    @Override
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
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getCategoryId() == null) {
            throw new IllegalArgumentException("类别ID不能为空，无法更新");
        }

        // 模拟图标上传并设置URL (如果图标数据有变化)
        // 假设以data:开头表示新上传的数据，或者根据实际情况判断是否需要重新上传
        if (categoryDTO.getIconFile() != null) {
            ResponseResult rr2 = this.fileUploadApi.upload(new MultipartFile[]{categoryDTO.getIconFile()});
            if (rr2.getCode() != 1) {
                throw new RuntimeException("类别主图片上传失败");
            }
            List<String> mainImages = (List<String>) rr2.getData();
            categoryDTO.setIcon(mainImages.get(0));
        }

        // 将DTO转换为实体
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 执行更新，MyBatis Plus会根据实体中的@TableId注解自动识别主键
        int result = categoryMapper.updateById(category);
        if (result > 0) {
            // 更新成功后，返回最新的类别信息
            return getCategoryById(categoryDTO.getCategoryId());
        } else {
            throw new RuntimeException("类别更新失败，类别ID: " + categoryDTO.getCategoryId());
        }
    }

    /**
     * 根据ID获取类别详情
     *
     * @param categoryId 类别ID
     * @return 类别DTO
     */
    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        BeanUtils.copyProperties(category, dto);
        return dto;
    }
}

