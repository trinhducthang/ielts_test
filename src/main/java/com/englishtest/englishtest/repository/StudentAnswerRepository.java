package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByUserId(long id);
}
