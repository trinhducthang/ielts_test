package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByPassageId(Long passageId);
}
