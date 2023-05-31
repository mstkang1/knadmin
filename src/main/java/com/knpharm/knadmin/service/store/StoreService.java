package com.knpharm.knadmin.service.store;

import com.knpharm.knadmin.dto.ProductDto;
import com.knpharm.knadmin.dto.StoreDto;

import java.util.List;

public interface StoreService {
    List<StoreDto> selectStoreList(StoreDto storeDto) throws Exception;

    int updateStoreProduct(ProductDto productDto) throws Exception;

    int insertStore(List<StoreDto> storeList) throws Exception;

    int insertStoreEach(StoreDto storeDto) throws Exception;

    int changeStore() throws Exception;

    int rollbackStore() throws Exception;

    int deleteStore(String productName) throws Exception;
}
