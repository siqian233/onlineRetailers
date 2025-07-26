package com.lightning.product.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lightning.model.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类数据访问接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
