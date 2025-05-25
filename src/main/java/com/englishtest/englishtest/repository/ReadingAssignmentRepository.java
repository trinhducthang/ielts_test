package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.reading.ReadingAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReadingAssignmentRepository extends MongoRepository<ReadingAssignment, String> {
    List<ReadingAssignment> findByAssignedUserIdsContains(Long userId);

}
