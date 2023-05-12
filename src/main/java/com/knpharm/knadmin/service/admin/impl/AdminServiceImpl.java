package com.knpharm.knadmin.service.admin.impl;

import com.knpharm.knadmin.dto.AdminDto;
import com.knpharm.knadmin.mapper.AdminMapper;
import com.knpharm.knadmin.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Optional<AdminDto> selectAdmin(AdminDto adminDto) throws UsernameNotFoundException {
        return adminMapper.selectAdmin(adminDto);
    }

    @Override
    public int insertAdmin(AdminDto adminDto) throws Exception {
        return adminMapper.insertAdmin(adminDto);
    }

    @Override
    public int updateAdmin(AdminDto adminDto) throws Exception {
        return adminMapper.updateAdmin(adminDto);
    }
}
