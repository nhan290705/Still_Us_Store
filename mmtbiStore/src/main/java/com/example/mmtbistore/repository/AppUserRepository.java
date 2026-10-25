package com.example.mmtbistore.repository;

import com.example.mmtbistore.model.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);   // dùng khi đăng nhập
    boolean existsByUsername(String username);           // kiểm tra trùng khi tạo nhân viên

    @Query("SELECT u FROM AppUser u WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<AppUser> searchStaff(@Param("search") String search, Pageable pageable);
}
