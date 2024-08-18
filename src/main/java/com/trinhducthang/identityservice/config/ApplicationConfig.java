package com.trinhducthang.identityservice.config;


import com.trinhducthang.identityservice.enums.Role;
import com.trinhducthang.identityservice.model.User;
import com.trinhducthang.identityservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class ApplicationConfig {


    @Bean
    ApplicationRunner applicationRunner(UserRepository usersRepository){
        return args -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            if(usersRepository.findByUsername("admin").isEmpty()){
                Role adminRole = Role.ADMIN;
                User users = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(adminRole)
                        .build();
                usersRepository.save(users);
                log.info("ADMIN saved successfully");
            }
        };
    }
}
