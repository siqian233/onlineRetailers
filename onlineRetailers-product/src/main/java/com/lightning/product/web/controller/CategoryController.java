package com.lightning.product.web.controller;

import com.lightning.product.service.CategoryService;
import com.lightning.web.bean.CategoryDTO;
import com.lightning.web.bean.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类API控制器
 * restful : http协议 : method: post, put, delete, get
 * 响应状态码: 200, 201, 204, 400, 401, 403, 404, 500   -》 返回值 ResponseEntity->
 * 业务问题: ResponseResult: code, msg, data
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {

    public static final Integer CATEGORY_STATUS_NORMAL = 1;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增类别
     * POST /category
     *
     * @param categoryDTO 待新增的类别信息
     * @return 包含新增成功后类别DTO的统一响应
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseResult<CategoryDTO>> addCategory(CategoryDTO categoryDTO) {
        try {
            CategoryDTO addedCategory = categoryService.addCategory(categoryDTO);
            addedCategory.setIconFile(null);
            ResponseResult<CategoryDTO> result = ResponseResult.ok("类别新增成功");
            // 类别创建成功，返回HTTP 201 Created，业务码1，并携带新增类别数据
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result.setData(addedCategory));
        } catch (Exception e) {
            // 捕获异常，返回HTTP 500 Internal Server Error，业务码0，并提示错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseResult.error("类别新增失败: " + e.getMessage()));
        }
    }

    /**
     * 查询所有类别
     * GET /category/all
     *
     * @return 包含所有类别DTO列表的统一响应
     */
    @GetMapping("/all")
    public ResponseEntity<ResponseResult<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories(CATEGORY_STATUS_NORMAL);
        ResponseResult<List<CategoryDTO>> result = ResponseResult.ok("查询所有类别成功");
        // 成功时，返回HTTP 200 OK，业务码1，并携带类别列表数据
        return ResponseEntity.ok(result.setData(categories));
    }

    /**
     * 设置类别状态为隐藏 (逻辑删除)
     * PUT /category/{categoryId}/{status}
     *
     * @param categoryId 类别ID
     * @return 操作结果的统一响应
     */
    @PutMapping("/{categoryId}/{status}")
    public ResponseEntity<ResponseResult<Void>> setCategoryStatus(@PathVariable("categoryId") Long categoryId, @PathVariable("status") Integer status) {
        boolean success = categoryService.setCategoryStatusToHidden(categoryId, status);
        if (success) {
            // 设置成功，返回HTTP 200 OK，业务码1
            return ResponseEntity.ok(ResponseResult.ok("修改类别状态成功"));
        } else {
            // 设置失败（例如类别不存在），返回HTTP 404 Not Found，业务码0
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseResult.error("设置类别状态失败，类别ID不存在或已隐藏: " + categoryId));
        }
    }

    /**
     * 更新类别信息
     * PUT /category/{categoryId}
     *
     * @param categoryId  类别ID
     * @param categoryDTO 待更新的类别信息
     * @return 包含更新成功后类别DTO的统一响应
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseResult<CategoryDTO>> updateCategory(@PathVariable("categoryId") Long categoryId, CategoryDTO categoryDTO) {
        if (!categoryId.equals(categoryDTO.getCategoryId())) {
            // 请求路径ID与DTO中的ID不一致，返回HTTP 400 Bad Request，业务码0
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseResult.error("请求路径中的类别ID与请求体中的类别ID不一致"));
        }
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO);
            ResponseResult<CategoryDTO> result = ResponseResult.ok("类别更新成功");
            // 更新成功，返回HTTP 200 OK，业务码1，并携带更新后的类别数据
            return ResponseEntity.ok(result.setData(updatedCategory));
        } catch (IllegalArgumentException e) {
            // 参数非法（例如类别ID为空），返回HTTP 400 Bad Request，业务码0
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseResult.error("请求参数错误: " + e.getMessage()));
        } catch (Exception e) {
            // 捕获其他异常，返回HTTP 500 Internal Server Error，业务码0
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseResult.error("类别更新失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取类别详情
     * GET /category/{categoryId}
     *
     * @param categoryId 类别ID
     * @return 包含类别DTO的统一响应
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseResult<CategoryDTO>> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        if (categoryDTO != null) {
            categoryDTO.setIconFile(null);
            ResponseResult<CategoryDTO> result = ResponseResult.ok("查询成功");

            // 找到类别，返回HTTP 200 OK，业务码1，并携带类别数据
            return ResponseEntity.ok(result.setData(categoryDTO));
        } else {
            // 未找到类别，返回HTTP 404 Not Found，业务码0，并提示错误信息
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseResult.error("类别不存在，ID: " + categoryId));
        }
    }
}