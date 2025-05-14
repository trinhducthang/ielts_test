package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.StudentAnswer;
import com.englishtest.englishtest.service.StudentAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/student-answer")
@RequiredArgsConstructor
public class StudentAnswerController {

    private final StudentAnswerService studentAnswerService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitExam(@RequestBody StudentAnswer studentAnswer) {
        try {
            studentAnswerService.saveStudentAnswer(studentAnswer);
            return ResponseEntity.ok("Submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving submission");
        }
    }
}
