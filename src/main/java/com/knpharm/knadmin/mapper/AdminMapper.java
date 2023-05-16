package com.knpharm.knadmin.mapper;

import com.knpharm.knadmin.dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminMapper {
    Optional<AdminDto> selectAdmin(String adminId);

    AdminDto selectAdminById(String adminId);

    int insertAdmin(AdminDto adminDto) throws Exception;

    int updateAdmin(AdminDto adminDto) throws Exception;

}
