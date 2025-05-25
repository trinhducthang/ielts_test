package com.englishtest.englishtest.repository;


import com.englishtest.englishtest.entity.ListeningExercise;
import com.englishtest.englishtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListeningExerciseRepository extends JpaRepository<ListeningExercise, Long> {
    List<ListeningExercise> findByAssignedStudentsContaining(User student);
}