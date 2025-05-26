package com.englishtest.englishtest.repository;

import com.englishtest.englishtest.entity.speaking.SpeakingAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpeakingAssignmentRepository extends MongoRepository<SpeakingAssignment, String> {
    List<SpeakingAssignment> findByAssignedUserIdsContaining(Long userId);
}
