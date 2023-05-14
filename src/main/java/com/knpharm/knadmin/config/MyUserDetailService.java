package com.knpharm.knadmin.config;

import com.knpharm.knadmin.dto.AdminDto;
import com.knpharm.knadmin.service.admin.AdminService;
import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyUserDetailService implements UserDetailsService {
    private final AdminService adminService;

    @Autowired
    public MyUserDetailService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String insertUserId) throws UsernameNotFoundException {

        Optional<AdminDto> findOne = adminService.selectAdmin(insertUserId);
        AdminDto adminDto = findOne.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 관리자입니다."));

        return User.builder()
                .username(adminDto.getAdminId())
                .password(adminDto.getAdminPass())
                /*.roles(adminDto.getRoles())*/
                .roles("ADMIN")
                .build();
    }
}
