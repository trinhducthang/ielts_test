package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.ListeningExercise;
import com.englishtest.englishtest.entity.StudentAnswer;
import com.englishtest.englishtest.service.ListeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/listening")
public class ListeningController {

    @Autowired
    private ListeningService service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExercise(
            @RequestParam String title,
            @RequestParam MultipartFile audio,
            @RequestParam MultipartFile pdf,
            @RequestParam int questionCount
    ) {
        if (questionCount < 1 || questionCount > 40) {
            return ResponseEntity.badRequest().body("Số câu hỏi phải từ 1 đến 40");
        }
        return ResponseEntity.ok(service.saveExercise(title, audio, pdf, questionCount));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<?> assignExerciseToStudents(@PathVariable Long id, @RequestBody List<Long> studentIds) {
        service.assignExerciseToStudents(id, studentIds);
        return ResponseEntity.ok("Đã giao bài thành công");
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitAnswers(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestBody List<String> answers
    ) {
        StudentAnswer answer = service.submitAnswers(id, username, answers);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/assigned")
    public ResponseEntity<?> getAssignedExercises(@RequestParam String username) {
        List<ListeningExercise> list = service.getExercisesForStudent(username);
        return ResponseEntity.ok(list);
    }
}

