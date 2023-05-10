package com.knpharm.knadmin.service.store;

import com.knpharm.knadmin.dto.StoreDto;

import java.util.List;

public interface StoreService {
    List<StoreDto> selectStoreList(StoreDto storeDto) throws Exception;
}
