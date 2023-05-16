package com.knpharm.knadmin.web;

import com.knpharm.knadmin.dto.AdminDto;
import com.knpharm.knadmin.dto.PopupDto;
import com.knpharm.knadmin.service.admin.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("login")
    public String login() {
        return "login";
    }
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public Map<String, Object> modify(@RequestParam("nowPass") String nowPass, @RequestParam("newPass") String newPass) throws Exception {

        Map<String, Object> rtnObj = new HashMap<>();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String adminId = userDetails.getUsername();

        AdminDto admin = adminService.selectAdminById(adminId);

        if(passwordEncoder.matches(nowPass, admin.getAdminPass())) {
            admin.setAdminPass(passwordEncoder.encode(newPass));
            adminService.updateAdmin(admin);

            rtnObj.put("result", "success");

        } else {
            rtnObj.put("result", "fail");
            rtnObj.put("message", "비밀번호가 틀립니다. 다시 확인하여 주십시오");
        }


        return rtnObj;
    }

}
