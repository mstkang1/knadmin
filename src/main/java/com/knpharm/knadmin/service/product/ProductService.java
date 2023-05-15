package com.knpharm.knadmin.service.product;

import com.knpharm.knadmin.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> selectProductList(String brandCode) throws Exception;

    ProductDto selectProduct(String productName) throws Exception;

    int updateProduct(ProductDto productDto) throws Exception;

}
