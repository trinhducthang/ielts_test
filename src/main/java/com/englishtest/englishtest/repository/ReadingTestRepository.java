package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.ReadingTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReadingTestRepository extends JpaRepository<ReadingTest, Long> {
    Page<ReadingTest> findAll(Pageable pageable);

    @Query("SELECT rt FROM ReadingTest rt " +
            "JOIN rt.courses c " +
            "JOIN c.users u " +
            "WHERE u.id = :userId")
    List<ReadingTest> findByUserId(@Param("userId") Long userId);

    Page<ReadingTest> findByIdIn(List<Long> ids, Pageable pageable);

}
