package com.knpharm.knadmin.mapper;

import com.knpharm.knadmin.dto.ProductDto;
import com.knpharm.knadmin.dto.StoreDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreMapper {
    List<StoreDto> selectStoreList(StoreDto storeDto) throws Exception;

    int updateStoreProduct(ProductDto productDto) throws Exception;

    int insertStore(List<StoreDto> storeList) throws Exception;

    int insertStoreEach(StoreDto storeDto) throws Exception;

    int changeStore() throws Exception;
    int rollbackStore() throws Exception;
}
