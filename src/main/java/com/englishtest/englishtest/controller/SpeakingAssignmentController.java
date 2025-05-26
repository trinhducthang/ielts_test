package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.entity.speaking.SpeakingAssignment;
import com.englishtest.englishtest.entity.writing.Feedback;
import com.englishtest.englishtest.entity.writing.Submission;
import com.englishtest.englishtest.repository.SpeakingAssignmentRepository;
import com.englishtest.englishtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/speaking")
public class SpeakingAssignmentController {

    @Autowired
    private SpeakingAssignmentRepository assignmentRepo;

    @Autowired
    private UserRepository userRepo;

    // Tạo bài speaking assignment mới
    @PostMapping("/assignments")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createAssignment(@RequestBody SpeakingAssignment assignment) {
        assignment.setTimeCreated(LocalDate.now());

        if (assignment.getDueDate() == null) {
            assignment.setDueDate(null);
        }

        assignmentRepo.save(assignment);
        return ResponseEntity.ok("Speaking assignment created successfully");
    }

    // Lấy bài nộp của học sinh theo assignment và userId
    @GetMapping("/assignments/{assignmentId}/submission/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getStudentSubmission(
            @PathVariable String assignmentId,
            @PathVariable Long userId) {

        SpeakingAssignment assignment = assignmentRepo.findById(assignmentId).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaking assignment not found.");
        }

        Optional<Submission> submission = assignment.getSubmissions().stream()
                .filter(sub -> sub.getUserId().equals(userId))
                .findFirst();

        if (submission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student has not submitted this assignment.");
        }

        return ResponseEntity.ok(submission.get());
    }

    // Lấy danh sách speaking assignments được giao cho học sinh đang đăng nhập
    @GetMapping("/assignments/mine")
    @PreAuthorize("hasRole('STUDENT')")
    public List<SpeakingAssignment> getAssignmentsForStudent() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authenticationName);
        return assignmentRepo.findByAssignedUserIdsContaining(student.getId());
    }

    // Lấy danh sách speaking assignments được giao cho học sinh theo username
    @GetMapping("/assignments/of")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<SpeakingAssignment>> getAssignmentsOfStudent(@RequestParam String username) {
        try {
            User student = userRepo.findByUsername(username);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
            List<SpeakingAssignment> assignments = assignmentRepo.findByAssignedUserIdsContaining(student.getId());
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Nộp bài speaking assignment
    @PostMapping("/assignments/{id}/submit")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> submitAssignment(
            @PathVariable String id,
            @RequestBody String content) {

        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authenticationName);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Student not found.");
        }

        SpeakingAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaking assignment not found.");
        }

        Optional<Submission> existingSubmission = assignment.getSubmissions().stream()
                .filter(submission -> submission.getUserId().equals(student.getId()))
                .findFirst();

        if (existingSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have already submitted this assignment.");
        }

        Submission submission = new Submission();
        submission.setUserId(student.getId());
        submission.setContent(content);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setSubmitted(true);

        assignment.getSubmissions().add(submission);

        assignmentRepo.save(assignment);

        return ResponseEntity.ok("Submission successful.");
    }

    // Thêm feedback cho submission speaking assignment
    @PostMapping("/assignments/{assignmentId}/feedback/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> giveFeedback(
            @PathVariable String assignmentId,
            @PathVariable Long userId,
            @RequestBody Feedback feedback) {

        SpeakingAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow();

        for (Submission submission : assignment.getSubmissions()) {
            if (submission.getUserId().equals(userId)) {
                submission.setFeedback(feedback);
                break;
            }
        }

        assignmentRepo.save(assignment);

        return ResponseEntity.ok("Feedback added successfully");
    }

    // Lấy submission của học sinh đang đăng nhập theo assignment id
    @GetMapping("/assignments/{id}/submission")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getSubmission(@PathVariable String id) {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authenticationName);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Student not found.");
        }

        SpeakingAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Speaking assignment not found.");
        }

        Optional<Submission> submission = assignment.getSubmissions().stream()
                .filter(sub -> sub.getUserId().equals(student.getId()))
                .findFirst();

        if (submission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You have not submitted this assignment.");
        }

        return ResponseEntity.ok(submission.get());
    }
}
