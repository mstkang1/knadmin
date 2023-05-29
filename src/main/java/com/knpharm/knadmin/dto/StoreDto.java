package com.knpharm.knadmin.dto;

import lombok.Data;


@Data
public class StoreDto {
    private int storeSeq;
    private String brandCode;
    private String productName;
    private String phCode;
    private String storeName;
    private String location1;
    private String location2;
    private String address;
    private String phone;
    private float latitude;
    private float longitude;
    private int distance;

}
