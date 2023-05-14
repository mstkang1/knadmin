package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.BannerDto;
import com.knpharm.knadmin.service.banner.BannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/banner")
public class BannerController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/list/{brandCode}")
    public  String banner(@PathVariable("brandCode") String brandCode, Model model) throws Exception {

        List<BannerDto> bannerList = bannerService.selecBannerList(brandCode);

        model.addAttribute("brandCode", brandCode);

        model.addAttribute("bannerList", bannerList);

        return "/banner/list";

    }

    @RequestMapping(value = "/edit/{brandCode}/{bannerSeq}")
    public  String modify(@PathVariable("brandCode") String brandCode, @PathVariable("bannerSeq") int bannerSeq, Model model) throws Exception {

        BannerDto banner = bannerService.selecBanner(bannerSeq);

        model.addAttribute("brandCode", brandCode);
        model.addAttribute("bannerSeq", bannerSeq);
        model.addAttribute("banner", banner);

        return "/banner/edit";
    }
}
