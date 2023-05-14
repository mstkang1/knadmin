package com.knpharm.knadmin.service.admin;

import com.knpharm.knadmin.dto.AdminDto;

import java.util.Optional;

public interface AdminService {
    Optional<AdminDto> selectAdmin(String adminId);

    int insertAdmin(AdminDto adminDto) throws Exception;

    int updateAdmin(AdminDto adminDto) throws Exception;
}
