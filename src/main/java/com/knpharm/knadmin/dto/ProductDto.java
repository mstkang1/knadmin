package com.knpharm.knadmin.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String productName;
    private String brandCode;
    private int productOrder;
    private String ProductOrgFileName;
    private String ProductSaveFileName;

    private String brandName;

    /*수정전 제품명*/
    private String oldProductName;

}
