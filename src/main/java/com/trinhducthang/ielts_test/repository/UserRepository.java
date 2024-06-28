package com.trinhducthang.ielts_test.repository;

import com.trinhducthang.ielts_test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
