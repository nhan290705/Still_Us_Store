package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);   // dùng khi đăng nhập
    boolean existsByUsername(String username);           // kiểm tra trùng khi tạo nhân viên
}
