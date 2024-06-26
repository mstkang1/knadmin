package com.knpharm.knadmin.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = {"/", "/index"})
    public  String index(Model model) {
        return "index";
    }

    @RequestMapping(value = "/pass")
    public  String pass(Model model) {
        return "pass";
    }

}
