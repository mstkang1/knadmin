package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.BannerDto;
import com.knpharm.knadmin.dto.PopupDto;
import com.knpharm.knadmin.service.Popup.PopupService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @RequestMapping(value = "/list/{brandCode}")
    public  String list(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        List<PopupDto> popupList = popupService.selectPopupList(brandCode);

        model.addAttribute("menuCode", "popup");
        model.addAttribute("brandCode", brandCode);

        model.addAttribute("popupList", popupList);

        return "popup/list";
    }

    @RequestMapping(value = "/edit/{brandCode}/{popupSeq}")
    public  String edit(@PathVariable("brandCode") String brandCode, @PathVariable("popupSeq") int popupSeq, Model model) throws Exception {

        PopupDto popup = popupService.selectPopup(popupSeq);

        model.addAttribute("menuCode", "popup");
        model.addAttribute("brandCode", brandCode);
        model.addAttribute("popupSeq", popupSeq);
        model.addAttribute("popup", popup);

        return "popup/edit";
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

                if(!(exten.equalsIgnoreCase(".jpg") || exten.equalsIgnoreCase(".jpeg") || exten.equalsIgnoreCase(".gif") ||
                        exten.equalsIgnoreCase(".png") || exten.equalsIgnoreCase(".bmp"))) {
                    rtnObj.put("result", "fail");
                    rtnObj.put("message", "잘못된 형식의 파일입니다.(jpg, jpeg, gif, png, bmp만 가능)");
                    return rtnObj;
                }
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

                if(!(exten.equalsIgnoreCase(".jpg") || exten.equalsIgnoreCase(".jpeg") || exten.equalsIgnoreCase(".gif") ||
                        exten.equalsIgnoreCase(".png") || exten.equalsIgnoreCase(".bmp"))) {
                    rtnObj.put("result", "fail");
                    rtnObj.put("message", "잘못된 형식의 파일입니다.(jpg, jpeg, gif, png, bmp만 가능)");
                    return rtnObj;
                }
            }
        }

        logger.info("popup -> " + popup.toString());

        popupService.updatePopup(popup);

        rtnObj.put("result", "success");
        return rtnObj;
    }

    @RequestMapping("/download/{pmFlag}/{popupSeq}")
    public void process(@PathVariable(name = "pmFlag") String pmFlag, @PathVariable(name = "popupSeq") int popupSeq, HttpServletResponse response) throws Exception {
        try {
            // 서비스를 통해 첨부파일 가져오기
            PopupDto popup = popupService.selectPopup(popupSeq);


            String originalName = "";
            String filePath = uploadPath + File.separatorChar;
            String fileName = "";

            if ("P".equals(pmFlag)) {
                // 파일명에 한글이 있는경우 처리
                originalName = new String(popup.getPopupPcOrgFileName().getBytes("utf-8"), "iso-8859-1");
                fileName = popup.getPopupPcSaveFileName();
            } else {
                // 파일명에 한글이 있는경우 처리
                originalName = new String(popup.getPopupMoOrgFileName().getBytes("utf-8"), "iso-8859-1");
                fileName = popup.getPopupMoSaveFileName();
            }

            String path = filePath + fileName;

            File file = new File(path);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

            //형식을 모르는 파일첨부용 contentType
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + originalName); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더


            //파일복사
            FileCopyUtils.copy(in, response.getOutputStream());
            in.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            throw new Exception("download error");

        }
    }
}
