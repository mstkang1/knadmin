package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.AdminDto;
import com.knpharm.knadmin.service.admin.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private AdminService adminService;

    @GetMapping("login")
    public String login() {
        return "/login";
    }
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

}
