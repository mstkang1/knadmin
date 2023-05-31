package com.knpharm.knadmin.service.store.impl;

import com.knpharm.knadmin.dto.ProductDto;
import com.knpharm.knadmin.dto.StoreDto;
import com.knpharm.knadmin.mapper.StoreMapper;
import com.knpharm.knadmin.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public List<StoreDto> selectStoreList(StoreDto storeDto) throws Exception {
        return storeMapper.selectStoreList(storeDto);
    }

    @Override
    public int updateStoreProduct(ProductDto productDto) throws Exception {
        return storeMapper.updateStoreProduct(productDto);
    }

    @Override
    public int insertStore(List<StoreDto> storeList) throws Exception {
        return storeMapper.insertStore(storeList);
    }

    @Override
    public int insertStoreEach(StoreDto storeDto) throws Exception {
        return storeMapper.insertStoreEach(storeDto);
    }

    @Override
    public int changeStore() throws Exception {
        return storeMapper.changeStore();
    }

    @Override
    public int rollbackStore() throws Exception {
        return storeMapper.rollbackStore();
    }

    @Override
    public int deleteStore(String productName) throws Exception {
        return storeMapper.deleteStore(productName);
    }

}
