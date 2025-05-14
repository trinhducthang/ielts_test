package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
