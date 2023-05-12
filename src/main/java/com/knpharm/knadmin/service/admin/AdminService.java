package com.knpharm.knadmin.service.admin;

import com.knpharm.knadmin.dto.AdminDto;

import java.util.Optional;

public interface AdminService {
    Optional<AdminDto> selectAdmin(AdminDto adminDto);

    int insertAdmin(AdminDto adminDto) throws Exception;

    int updateAdmin(AdminDto adminDto) throws Exception;
}
