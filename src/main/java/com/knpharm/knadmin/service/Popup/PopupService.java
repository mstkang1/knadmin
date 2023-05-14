package com.knpharm.knadmin.service.Popup;


import com.knpharm.knadmin.dto.PopupDto;

import java.util.List;

public interface PopupService {

    List<PopupDto> selecPopupList(String brandCode) throws Exception;

    PopupDto selecPopup(int popupSeq) throws Exception;

    int insertPopup(PopupDto popupDto) throws Exception;

    int updatePopup(PopupDto popupDto) throws Exception;


}
