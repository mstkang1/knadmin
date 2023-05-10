package com.knpharm.knadmin.mapper;

import com.knpharm.knadmin.dto.StoreDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoreMapper {
    List<StoreDto> selectStoreList(StoreDto storeDto) throws Exception;
}
