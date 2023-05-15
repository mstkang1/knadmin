package com.knpharm.knadmin.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PopupDto {
    private int popupSeq;
    private String brandCode;
    private boolean popupIsShow;
    private Date popupStartDate;
    private Date popupEndDate;
    private String popupStrStartDate;
    private String popupStrEndDate;
    private String popupName;
    private String popupPcUrl;
    private String popupPcOrgFileName;
    private String popupPcSaveFileName;
    private boolean popupPcIsNew;
    private String popupMoUrl;
    private String popupMoOrgFileName;
    private String popupMoSaveFileName;
    private boolean popupMoIsNew;

    private String brandName;
}
