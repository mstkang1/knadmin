package com.knpharm.knadmin.mapper;

import com.knpharm.knadmin.dto.BannerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerMapper {
    List<BannerDto> selectBannerList(String brandCode) throws Exception;

    BannerDto selectBanner(int bannerSeq) throws Exception;

    int insertBanner(BannerDto bannerDto) throws Exception;

    int updateBanner(BannerDto bannerDto) throws Exception;
}
