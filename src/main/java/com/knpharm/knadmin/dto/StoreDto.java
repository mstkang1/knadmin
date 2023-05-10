package com.knpharm.knadmin.dto;

import lombok.Data;


@Data
public class StoreDto {
    private int storeSeq;

    private String brandCode;

    private String productName;

    private String storeName;

    private String address;

    private float latitude;

    private float longitude;

    private int distance;

}
