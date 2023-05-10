package com.knpharm.knadmin.mapper;

import com.knpharm.knadmin.dto.ProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductDto> selectProductList(ProductDto productDto) throws Exception;
}
