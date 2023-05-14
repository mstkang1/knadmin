package com.knpharm.knadmin.service.banner.impl;

import com.knpharm.knadmin.dto.AdminDto;
import com.knpharm.knadmin.dto.BannerDto;
import com.knpharm.knadmin.mapper.BannerMapper;
import com.knpharm.knadmin.service.banner.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public List<BannerDto> selecBannerList(String  brandCode) throws Exception {
        return bannerMapper.selectBannerList(brandCode);
    }

    @Override
    public BannerDto selecBanner(int  bannerSeq) throws Exception {
        return bannerMapper.selectBanner(bannerSeq);
    }

    @Override
    public int insertBanner(BannerDto bannerDto) throws Exception {
        return bannerMapper.insertBanner(bannerDto);
    }

    @Override
    public int updateBanner(BannerDto bannerDto) throws Exception {
        return bannerMapper.updateBanner(bannerDto);
    }
}
