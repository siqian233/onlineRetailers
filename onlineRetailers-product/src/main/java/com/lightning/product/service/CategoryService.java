package com.lightning.product.service;

import com.lightning.web.bean.CategoryDTO;

import java.util.List;

/**
 * 商品分类服务接口
 */
public interface CategoryService {

    /**
     * 新增类别
     * @param categoryDTO 待新增的类别信息
     * @return 新增成功后的类别DTO
     */
    CategoryDTO addCategory(CategoryDTO categoryDTO);

    /**
     * 根据状态查询所有类别
     * @return 所有类别DTO的列表
     */
    List<CategoryDTO> getAllCategories(Integer categoryStatus);

    /**
     * 修改类别状态
     * @param categoryId 类别ID
     * @return 是否设置成功
     */
    boolean setCategoryStatusToHidden(Long categoryId, Integer categoryStatus);

    /**
     * 更新类别信息
     * @param categoryDTO 待更新的类别信息
     * @return 更新成功后的类别DTO
     */
    CategoryDTO updateCategory(CategoryDTO categoryDTO);

    /**
     * 根据ID获取类别详情
     * @param categoryId 类别ID
     * @return 类别DTO
     */
    CategoryDTO getCategoryById(Long categoryId);
}


