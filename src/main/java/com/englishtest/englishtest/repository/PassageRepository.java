package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.Passage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PassageRepository extends JpaRepository<Passage, Long> {
    List<Passage> findByReadingTestId(Long readingTestId);
}
