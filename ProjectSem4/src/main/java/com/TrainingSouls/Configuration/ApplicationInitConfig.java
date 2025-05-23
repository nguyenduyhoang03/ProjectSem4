package com.TrainingSouls.Configuration;

import com.TrainingSouls.Entity.Role;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Repository.RoleRepository;
import com.TrainingSouls.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            // Kiểm tra và tạo role ADMIN nếu chưa có
            if (!roleRepository.existsById("ADMIN")) {
                Role adminRole = Role.builder()
                        .name("ADMIN")
                        .description("Administrator")
                        .build();
                roleRepository.save(adminRole);
                log.warn("Đã tạo role ADMIN");
            }

            // Tạo user admin nếu chưa tồn tại
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                Role adminRole = roleRepository.findById("ADMIN").orElseThrow();

                User user = User.builder()
                        .name("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("12345678"))
                        .accountType("Premium")
                        .points(99999999)
                        .level(99)
                        .active(true)
                        .roles(Set.of(adminRole)) // thêm role
                        .build();

                userRepository.save(user);
                log.warn("Đã tạo tài khoản ADMIN");
            }
        };
    }
}
