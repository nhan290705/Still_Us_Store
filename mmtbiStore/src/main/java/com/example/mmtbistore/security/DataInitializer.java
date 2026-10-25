package com.example.mmtbistore.security;

import com.example.mmtbistore.model.enums.UserRole;
import com.example.mmtbistore.model.user.AppUser;
import com.example.mmtbistore.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.admin.init.username:admin}")
    private String adminUsername;

    @Value("${security.admin.init.password:Admin@123456}")
    private String adminPassword;

    @Value("${security.admin.init.full-name:Quản trị viên}")
    private String adminFullName;

    @Override
    public void run(String... args) {
        if (!appUserRepository.existsByUsername(adminUsername)) {
            log.info("Khởi tạo tài khoản admin mặc định: {}", adminUsername);
            AppUser admin = new AppUser();
            admin.setUsername(adminUsername);
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setFullName(adminFullName);
            admin.setRole(UserRole.ADMIN);
            admin.setActive(true);
            admin.setEmail("admin@stillus.local");
            admin.setPhoneNumber("0900000000");

            appUserRepository.save(admin);
            log.info("Tạo tài khoản admin mặc định thành công!");
        }

        // Tạo sẵn tài khoản mẫu dừng hợp tác (is_active = false) để phục vụ test 403
        if (!appUserRepository.existsByUsername("locked_staff")) {
            AppUser lockedUser = new AppUser();
            lockedUser.setUsername("locked_staff");
            lockedUser.setPasswordHash(passwordEncoder.encode("Staff@123456"));
            lockedUser.setFullName("Nhân viên dừng hợp tác");
            lockedUser.setRole(UserRole.STAFF);
            lockedUser.setActive(false);
            lockedUser.setEmail("locked@stillus.local");
            lockedUser.setPhoneNumber("0911111111");

            appUserRepository.save(lockedUser);
        }
    }
}
