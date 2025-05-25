package com.englishtest.englishtest.service;

import com.englishtest.englishtest.entity.*;
import com.englishtest.englishtest.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ListeningService {

    @Value("${upload.path}")
    private String uploadPath;

    private final ListeningExerciseRepository exerciseRepo;
    private final StudentAnswerRepository answerRepo;
    private final UserRepository userRepo;

    public ListeningService(ListeningExerciseRepository exerciseRepo,
                            StudentAnswerRepository answerRepo,
                            UserRepository userRepo) {
        this.exerciseRepo = exerciseRepo;
        this.answerRepo = answerRepo;
        this.userRepo = userRepo;
    }

    public ListeningExercise saveExercise(String title, MultipartFile audio, MultipartFile pdf, int count) {
        String audioPath = saveFile(audio);
        String pdfPath = saveFile(pdf);

        ListeningExercise ex = ListeningExercise.builder()
                .title(title)
                .audioFilePath(audioPath)
                .pdfFilePath(pdfPath)
                .questionCount(count)
                .build();

        return exerciseRepo.save(ex);
    }

    public void assignExerciseToStudents(Long exerciseId, List<Long> studentIds) {
        ListeningExercise ex = exerciseRepo.findById(exerciseId).orElseThrow();
        List<User> students = userRepo.findAllById(studentIds);

        ex.setAssignedStudents(students);
        exerciseRepo.save(ex);
    }

    public StudentAnswer submitAnswers(Long exerciseId, String username, List<String> answers) {
        ListeningExercise ex = exerciseRepo.findById(exerciseId).orElseThrow();
        User student = userRepo.findByUsername(username);

        if (!ex.getAssignedStudents().contains(student)) {
            throw new RuntimeException("Học sinh chưa được giao bài này");
        }

        if (answers.size() != ex.getQuestionCount()) {
            throw new RuntimeException("Số đáp án không khớp số câu hỏi");
        }

        StudentAnswer ans = StudentAnswer.builder()
                .exercise(ex)
                .student(student)
                .answers(answers)
                .build();

        return answerRepo.save(ans);
    }

    public List<ListeningExercise> getExercisesForStudent(String username) {
        User student = userRepo.findByUsername(username);
        return exerciseRepo.findByAssignedStudentsContaining(student);
    }

    private String saveFile(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath, filename);
        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi lưu file: " + e.getMessage());
        }
        return filename;
    }
}
