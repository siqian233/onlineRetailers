package com.lightning.web.api;

import com.lightning.web.bean.ProductDTO;
import com.lightning.web.bean.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "product")
public interface ProductApi {
    @RequestMapping(value = "/product/product/add", method = RequestMethod.POST)
    ResponseResult addProduct(@RequestBody ProductDTO productDTO);

}
