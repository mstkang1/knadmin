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
    AdminDto adminDto = new AdminDto();
    adminDto.setAdminId(insertUserId);
        Optional<AdminDto> findOne = adminService.selectAdmin(adminDto);
        AdminDto loginAdmin = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다 ㅠ"));

        return User.builder()
                .username(loginAdmin.getAdminId())
                .password(loginAdmin.getAdminPass())
                /*.roles(member.getRoles())*/
                .roles("ADMIN")
                .build();
    }
}
