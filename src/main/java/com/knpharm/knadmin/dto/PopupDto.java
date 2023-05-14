package com.knpharm.knadmin.dto;

import lombok.Data;

@Data
public class PopupDto {
    private int popupSeq;
    private String brandCode;
    private boolean popupIsShow;
    private String popupStartDate;
    private String popupEndDate;
    private String popupName;
    private String popupPcUrl;
    private String popupPcOrgFileName;
    private String popupPcSaveFileName;
    private boolean popupPcIsNew;
    private String popupMoUrl;
    private String popupMoOrgFileName;
    private String popupMoSaveFileName;
    private boolean babnnerMoIsNew;
}
