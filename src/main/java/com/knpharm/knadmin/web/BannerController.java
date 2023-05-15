package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.BannerDto;
import com.knpharm.knadmin.service.banner.BannerService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    /*@Value("${spring.multipart.location}")*/
    private String uploadPath;

    @RequestMapping(value = "/list/{brandCode}")
    public String list(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        List<BannerDto> bannerList = bannerService.selecBannerList(brandCode);

        model.addAttribute("menuCode", "banner");
        model.addAttribute("brandCode", brandCode);

        model.addAttribute("bannerList", bannerList);

        return "/banner/list";

    }

    @RequestMapping(value = "/edit/{brandCode}/{bannerSeq}")
    public String edit(@PathVariable("brandCode") String brandCode, @PathVariable("bannerSeq") int bannerSeq, Model model) throws Exception {

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
            BannerDto banner, @RequestParam("bannerFile") MultipartFile[] files) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        if (files.length < 1) {
            rtnObj.put("result", "fail");
            rtnObj.put("message", "잘못된 접근입니다.");
            return rtnObj;
        } else {
            MultipartFile pcFile = files[0];
            if (!pcFile.isEmpty()) {
                String originName = pcFile.getOriginalFilename();//파일이름
                String exten = originName.substring(originName.lastIndexOf("."), originName.length());//확장자
                String random = "";//랜덤값
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                int rndNum = (int) (Math.random() * 100000);
                random = sdf.format(new Date(System.currentTimeMillis())) + "_" + rndNum + exten;
                pcFile.transferTo(new File(random));

                banner.setBannerPcOrgFileName(originName);
                banner.setBannerPcSaveFileName(random);
            }

            MultipartFile moFile = files[1];
            if (!moFile.isEmpty()) {
                String originName = moFile.getOriginalFilename();//파일이름
                String exten = originName.substring(originName.lastIndexOf("."), originName.length());//확장자
                String random = "";//랜덤값
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmssSSS");
                int rndNum = (int) (Math.random() * 100000);
                random = sdf.format(new Date(System.currentTimeMillis())) + "_" + rndNum + exten;
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

    // @PathVariable 사용하여 url상의 경로를 변수에 할당 "/attach/download/25625"
    @RequestMapping("/download/{pmFlag}/{bannerSeq}")
    public void process(@PathVariable(name = "pmFlag") String pmFlag, @PathVariable(name = "bannerSeq") int bannerSeq, HttpServletResponse response) throws Exception {
        try {
            // 서비스를 통해 첨부파일 가져오기
            BannerDto banner = bannerService.selecBanner(bannerSeq);


            String originalName = "";
            String filePath = uploadPath + File.separatorChar;
            String fileName = "";

            if ("P".equals(pmFlag)) {
                // 파일명에 한글이 있는경우 처리
                originalName = new String(banner.getBannerPcOrgFileName().getBytes("utf-8"), "iso-8859-1");
                fileName = banner.getBannerPcSaveFileName();
            } else {
                // 파일명에 한글이 있는경우 처리
                originalName = new String(banner.getBannerMoOrgFileName().getBytes("utf-8"), "iso-8859-1");
                fileName = banner.getBannerMoSaveFileName();
            }

            String path = filePath + fileName;

            File file = new File(path);
            response.setHeader("Content-Disposition", "attachment;filename=" + originalName); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

            FileInputStream fileInputStream = new FileInputStream(path); // 파일 읽어오기
            OutputStream out = response.getOutputStream();

            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = fileInputStream.read(buffer)) != -1) { // 1024바이트씩 계속 읽으면서 outputStream에 저장, -1이 나오면 더이상 읽을 파일이 없음
                out.write(buffer, 0, read);
            }
        } catch (Exception e) {
            throw new Exception("download error");

        }
    }
}

