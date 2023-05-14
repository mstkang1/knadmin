package com.knpharm.knadmin.dto;

import lombok.Data;

@Data
public class BannerDto {
    private int bannerSeq;
    private String brandCode;
    private int bannerOrder;
    private String bannerName;
    private String bannerPcUrl;
    private String bannerPcOrgFileName;
    private String bannerPcSaveFileName;
    private boolean bannerPcIsNew;
    private String bannerMoUrl;
    private String bannerMoOrgFileName;
    private String bannerMoSaveFileName;
    private boolean bannerMoIsNew;
}
