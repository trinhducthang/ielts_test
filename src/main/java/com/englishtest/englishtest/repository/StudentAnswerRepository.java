package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
}
