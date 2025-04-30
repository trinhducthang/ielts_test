package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User getUserByUsername(String username);
}
