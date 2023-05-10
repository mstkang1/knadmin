package com.knpharm.knadmin.service.product.impl;

import com.knpharm.knadmin.dto.ProductDto;
import com.knpharm.knadmin.mapper.ProductMapper;
import com.knpharm.knadmin.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<ProductDto> selectProductList(ProductDto productDto) throws Exception {
        return productMapper.selectProductList(productDto);
    }
}
