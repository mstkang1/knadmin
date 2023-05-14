package com.knpharm.knadmin.service.banner;

import com.knpharm.knadmin.dto.BannerDto;

import java.util.List;

public interface BannerService {
    List<BannerDto> selecBannerList(String brandCode) throws Exception;

    BannerDto selecBanner(int bannerSeq) throws Exception;

    int insertBanner(BannerDto bannerDto) throws Exception;

    int updateBanner(BannerDto bannerDto) throws Exception;


}
