package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.BannerDto;
import com.knpharm.knadmin.service.banner.BannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/banner")
public class BannerController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/list/{brandCode}")
    public  String list(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        List<BannerDto> bannerList = bannerService.selecBannerList(brandCode);

        model.addAttribute("menuCode", "banner");
        model.addAttribute("brandCode", brandCode);

        model.addAttribute("bannerList", bannerList);

        return "/banner/list";

    }

    @RequestMapping(value = "/edit/{brandCode}/{bannerSeq}")
    public  String modify(@PathVariable("brandCode") String brandCode, @PathVariable("bannerSeq") int bannerSeq, Model model) throws Exception {

        BannerDto banner = bannerService.selecBanner(bannerSeq);

        model.addAttribute("menuCode", "banner");
        model.addAttribute("brandCode", brandCode);
        model.addAttribute("bannerSeq", bannerSeq);
        model.addAttribute("banner", banner);

        return "/banner/edit";
    }



    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, Object> modify(
            BannerDto banner,@RequestParam("bannerFile") MultipartFile[] files ) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

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

                banner.setBannerPcOrgFileName(originName);
                banner.setBannerPcSaveFileName(random);
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

                banner.setBannerMoOrgFileName(originName);
                banner.setBannerMoSaveFileName(random);
            }
        }

        logger.info("banner -> " + banner.toString());

        bannerService.updateBanner(banner);

        rtnObj.put("result", "success");
        return rtnObj;
    }
}
