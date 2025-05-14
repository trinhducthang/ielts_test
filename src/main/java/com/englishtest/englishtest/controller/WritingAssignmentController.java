package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.entity.writing.Feedback;
import com.englishtest.englishtest.entity.writing.Submission;
import com.englishtest.englishtest.entity.writing.WritingAssignment;
import com.englishtest.englishtest.repository.UserRepository;
import com.englishtest.englishtest.repository.writing.WritingAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WritingAssignmentController {

    @Autowired
    private WritingAssignmentRepository assignmentRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/assignments")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createAssignmentWithImage(
            @RequestBody WritingAssignment assignment) {

        assignment.setTimeCreated(LocalDate.now());

        if (assignment.getDueDate() == null) {
            assignment.setDueDate(null);
        }

        // Lưu bài viết vào MongoDB
        assignmentRepo.save(assignment);

        return ResponseEntity.ok("Assignment created successfully");
    }

    @GetMapping("/assignments/{assignmentId}/submission/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getStudentSubmissionForAssignment(
            @PathVariable String assignmentId,
            @PathVariable Long userId) {

        WritingAssignment assignment = assignmentRepo.findById(assignmentId).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bài viết không tồn tại.");
        }

        Optional<Submission> submission = assignment.getSubmissions().stream()
                .filter(sub -> sub.getUserId().equals(userId))
                .findFirst();

        if (submission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Học sinh chưa nộp bài này.");
        }

        return ResponseEntity.ok(submission.get());
    }

    @GetMapping("/assignments/mine")
    @PreAuthorize("hasRole('STUDENT')")
    public List<WritingAssignment> getAssignmentsForStudent() {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authenticationName);
        return assignmentRepo.findByAssignedUserIdsContaining(student.getId());
    }

    @GetMapping("/assignments/of")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<WritingAssignment>> getAssignmentsOfStudent(@RequestParam String username) {
        try {
            User student = userRepo.findByUsername(username);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
            List<WritingAssignment> assignments = assignmentRepo.findByAssignedUserIdsContaining(student.getId());
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/assignments/{id}/submit")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')")
    public ResponseEntity<?> submitAssignment(
            @PathVariable String id,
            @RequestBody String content) {

        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authenticationName);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Học sinh không tồn tại.");
        }

        WritingAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bài viết không tồn tại.");
        }

        Optional<Submission> existingSubmission = assignment.getSubmissions().stream()
                .filter(submission -> submission.getUserId().equals(student.getId()))
                .findFirst();

        if (existingSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bạn đã nộp bài này rồi.");
        }

        Submission submission = new Submission();
        submission.setUserId(student.getId());
        submission.setContent(content);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setSubmitted(true); // ✅ Cập nhật ở đây

        assignment.getSubmissions().add(submission);

        assignmentRepo.save(assignment);

        return ResponseEntity.ok("Nộp bài thành công.");
    }

    @PostMapping("/assignments/{assignmentId}/feedback/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> giveFeedback(
            @PathVariable String assignmentId,
            @PathVariable Long userId,
            @RequestBody Feedback feedback) {

        WritingAssignment assignment = assignmentRepo.findById(assignmentId)
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


    @GetMapping("/assignments/{id}/submission")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getSubmission(@PathVariable String id) {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authenticationName);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Học sinh không tồn tại.");
        }

        WritingAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bài viết không tồn tại.");
        }

        Optional<Submission> submission = assignment.getSubmissions().stream()
                .filter(sub -> sub.getUserId().equals(student.getId()))
                .findFirst();

        if (submission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bạn chưa nộp bài này.");
        }

        return ResponseEntity.ok(submission.get());
    }
}
