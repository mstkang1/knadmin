package com.knpharm.knadmin.service.product;

import com.knpharm.knadmin.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> selectProductList(String brandCode) throws Exception;

    ProductDto selectProduct(int productSeq) throws Exception;

    int updateProduct(ProductDto productDto) throws Exception;

}
