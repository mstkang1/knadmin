package com.knpharm.knadmin.service.product;

import com.knpharm.knadmin.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> selectProductList(ProductDto productDto) throws Exception;

}
