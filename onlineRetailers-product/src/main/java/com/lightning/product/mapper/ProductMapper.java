package com.lightning.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lightning.model.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}

