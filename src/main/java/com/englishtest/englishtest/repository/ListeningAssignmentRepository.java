package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.listening.ListeningAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ListeningAssignmentRepository extends MongoRepository<ListeningAssignment, String> {
    List<ListeningAssignment> findByAssignedUserIdsContains(Long userId);
}
