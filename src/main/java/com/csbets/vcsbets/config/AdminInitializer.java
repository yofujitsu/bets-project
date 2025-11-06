package com.csbets.vcsbets.config;

import com.csbets.vcsbets.entity.user.User;
import com.csbets.vcsbets.entity.user.UserRole;
import com.csbets.vcsbets.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;

    @PostConstruct
    public void initAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setId(1L);
            admin.setUsername("admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("0wkMxGeU#PHc"));
            admin.setRole(UserRole.ADMIN);
            admin.setCreditBalance((short) 0);
            userRepository.save(admin);
        }
    }
}
