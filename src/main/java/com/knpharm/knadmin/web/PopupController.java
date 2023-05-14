package com.knpharm.knadmin.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/popup")
public class PopupController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/list")
    public  String banner(Model model) {
        return "/popup/list";
    }

    @RequestMapping(value = "/edit")
    public  String modify(Model model) {
        return "/popup/edit";
    }
}
