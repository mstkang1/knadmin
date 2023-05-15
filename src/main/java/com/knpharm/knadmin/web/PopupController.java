package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.BannerDto;
import com.knpharm.knadmin.dto.PopupDto;
import com.knpharm.knadmin.service.Popup.PopupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/popup")
public class PopupController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private PopupService popupService;

    @RequestMapping(value = "/list/{brandCode}")
    public  String list(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        List<PopupDto> popupList = popupService.selectPopupList(brandCode);

        model.addAttribute("menuCode", "popup");
        model.addAttribute("brandCode", brandCode);

        model.addAttribute("popupList", popupList);

        return "/popup/list";
    }

    @RequestMapping(value = "/edit/{brandCode}/{popupSeq}")
    public  String edit(@PathVariable("brandCode") String brandCode, @PathVariable("popupSeq") int popupSeq, Model model) throws Exception {

        PopupDto popup = popupService.selectPopup(popupSeq);

        model.addAttribute("menuCode", "popup");
        model.addAttribute("brandCode", brandCode);
        model.addAttribute("popupSeq", popupSeq);
        model.addAttribute("popup", popup);

        return "/popup/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, Object> modify(
            PopupDto popup,@RequestParam("popupFile") MultipartFile[] files ) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        // 포맷터
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");

        popup.setPopupStartDate((Date)formatter.parse(popup.getPopupStrStartDate()));
        popup.setPopupEndDate((Date)formatter.parse(popup.getPopupStrEndDate()));

        if(files.length < 1) {
            rtnObj.put("result", "fail");
            rtnObj.put("message", "잘못된 접근입니다.");
            return rtnObj;
        } else {
            MultipartFile pcFile = files[0];
            if(!pcFile.isEmpty()) {
                String originName = pcFile.getOriginalFilename();//파일이름
                String exten = originName.substring(originName.lastIndexOf("."),originName.length());//확장자
                String random = "";//랜덤값
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                int rndNum = (int)(Math.random()*100000);
                random = sdf.format(new Date(System.currentTimeMillis()))+"_"+rndNum+exten;
                pcFile.transferTo(new File(random));

                popup.setPopupPcOrgFileName(originName);
                popup.setPopupPcSaveFileName(random);
            }

            MultipartFile moFile = files[1];
            if(!moFile.isEmpty()) {
                String originName = moFile.getOriginalFilename();//파일이름
                String exten = originName.substring(originName.lastIndexOf("."),originName.length());//확장자
                String random = "";//랜덤값
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                int rndNum = (int)(Math.random()*100000);
                random = sdf.format(new Date(System.currentTimeMillis()))+"_"+rndNum+exten;
                moFile.transferTo(new File(random));

                popup.setPopupMoOrgFileName(originName);
                popup.setPopupMoSaveFileName(random);
            }
        }

        logger.info("popup -> " + popup.toString());

        popupService.updatePopup(popup);

        rtnObj.put("result", "success");
        return rtnObj;
    }
}
