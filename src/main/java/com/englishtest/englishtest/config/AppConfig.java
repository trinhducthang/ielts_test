package com.englishtest.englishtest.config;


import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.entity._enum.Role;
import com.englishtest.englishtest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AppConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository usersRepository){
        return args -> {
            if(usersRepository.findByUsername("admin@") == null){
                Role adminRole = Role.ADMIN;
                User users = User.builder()
                        .username("admin@")
                        .password(passwordEncoder.encode("admin@"))
                        .fullName("Hoang Nga")
                        .role(adminRole)
                        .build();
                usersRepository.save(users);
                log.info("ADMIN saved successfully");
            }
        };
    }
}