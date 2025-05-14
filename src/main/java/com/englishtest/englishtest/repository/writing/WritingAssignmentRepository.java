package com.englishtest.englishtest.repository.writing;

import com.englishtest.englishtest.entity.writing.WritingAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WritingAssignmentRepository extends MongoRepository<WritingAssignment, String> {
    List<WritingAssignment> findByAssignedUserIdsContaining(Long userId);
}

