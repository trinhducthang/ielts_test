package com.englishtest.englishtest.service;

import com.englishtest.englishtest.entity.StudentAnswer;
import com.englishtest.englishtest.repository.StudentAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;

    public StudentAnswer saveStudentAnswer(StudentAnswer studentAnswer) {
        return studentAnswerRepository.save(studentAnswer);
    }
}
