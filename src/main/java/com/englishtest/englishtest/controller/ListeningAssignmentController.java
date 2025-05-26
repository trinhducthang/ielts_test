package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.dto.ExcelReader;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.entity.listening.ListeningAssignment;
import com.englishtest.englishtest.entity.listening.ListeningSubmission;
import com.englishtest.englishtest.entity.QuestionAnswer;
import com.englishtest.englishtest.repository.ListeningAssignmentRepository;
import com.englishtest.englishtest.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/listening")
public class ListeningAssignmentController {

    @Autowired
    private ListeningAssignmentRepository assignmentRepo;

    @Autowired
    private UserRepository userRepo;

    private final String UPLOAD_DIR = "D:/englishtest/uploads";

    private String saveFileAndGetUrl(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return "/uploads/" + file.getOriginalFilename();
    }

    // Tạo bài nghe mới
    @PostMapping("/assignments")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createListeningAssignment(
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("excelFile") MultipartFile excelFile,
            @RequestParam("description") String description,
            @RequestParam("dueDate") String dueDateStr,
            @RequestParam("assignedUserIds") String assignedUserIdsJson
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> assignedUserIds = objectMapper.readValue(assignedUserIdsJson, List.class);

            String pdfUrl = saveFileAndGetUrl(pdfFile);
            String audioUrl = saveFileAndGetUrl(audioFile);

            List<QuestionAnswer> answerKey = ExcelReader.readAnswerKeyFromExcel(excelFile.getInputStream());

            ListeningAssignment assignment = new ListeningAssignment();
            assignment.setDescription(description);
            assignment.setTimeCreated(LocalDate.now());
            assignment.setDueDate(LocalDate.parse(dueDateStr));
            assignment.setReadingPdfUrl(pdfUrl);
            assignment.setAudioMp3Url(audioUrl);
            assignment.setAssignedUserIds(assignedUserIds);
            assignment.setAnswerKey(answerKey);

            assignmentRepo.save(assignment);

            return ResponseEntity.ok("Listening assignment created successfully");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }

    // Lấy bài nghe theo ID
    @GetMapping("/assignments/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable String id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập");
        }

        Optional<ListeningAssignment> opt = assignmentRepo.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bài nghe");
        }

        ListeningAssignment assignment = opt.get();

        Map<String, Object> result = new HashMap<>();
        result.put("id", assignment.getId());
        result.put("description", assignment.getDescription());
        result.put("pdfUrl", assignment.getReadingPdfUrl());
        result.put("audioUrl", assignment.getAudioMp3Url());
        result.put("totalQuestions", assignment.getAnswerKey().size());

        return ResponseEntity.ok(result);
    }

    // Lấy bài nghe của học sinh
    @GetMapping("/assignments/mine")
    @PreAuthorize("hasRole('STUDENT')")
    public List<ListeningAssignment> getAssignmentsForStudent() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(username);
        return assignmentRepo.findByAssignedUserIdsContains(student.getId());
    }

    // Học sinh nộp bài
    @PostMapping("/assignments/{id}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitAssignment(
            @PathVariable String id,
            @RequestBody ListeningSubmission submissionRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(username);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }

        ListeningAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
        }

        boolean alreadySubmitted = assignment.getSubmissions().stream()
                .anyMatch(s -> s.getUserId().equals(student.getId()));
        if (alreadySubmitted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already submitted this assignment");
        }

        int correctCount = 0;
        Map<Integer, String> studentAnswers = submissionRequest.getAnswers();
        for (QuestionAnswer qa : assignment.getAnswerKey()) {
            String studentAnswer = studentAnswers.get(qa.getQuestionNumber());
            if (studentAnswer != null && studentAnswer.equalsIgnoreCase(qa.getCorrectAnswer())) {
                correctCount++;
            }
        }

        submissionRequest.setUserId(student.getId());
        submissionRequest.setSubmittedAt(LocalDateTime.now());
        submissionRequest.setSubmitted(true);
        submissionRequest.setScore(correctCount);

        assignment.getSubmissions().add(submissionRequest);
        assignmentRepo.save(assignment);

        return ResponseEntity.ok("Submission successful. Score: " + correctCount);
    }

    // Giáo viên xem bài nộp của học sinh
    @GetMapping("/assignments/{assignmentId}/submission/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getStudentSubmission(
            @PathVariable String assignmentId,
            @PathVariable Long userId
    ) {
        ListeningAssignment assignment = assignmentRepo.findById(assignmentId).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Assignment not found"));
        }

        return assignment.getSubmissions().stream()
                .filter(s -> s.getUserId().equals(userId))
                .findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Submission not found")));
    }

    // Học sinh xem bài đã nộp
    @GetMapping("/assignments/{id}/submission")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getSubmission(@PathVariable String id) {
        String authName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authName);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }

        ListeningAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
        }

        Optional<ListeningSubmission> submission = assignment.getSubmissions().stream()
                .filter(sub -> sub.getUserId().equals(student.getId()))
                .findFirst();

        return submission
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No submission found"));
    }


    // Giáo viên lấy danh sách bài nghe của học sinh
    @GetMapping("/assignments/of")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getAssignmentsOfStudent(@RequestParam String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<ListeningAssignment> assigned = assignmentRepo.findAll().stream()
                .filter(a -> a.getAssignedUserIds().contains(user.getId()))
                .toList();

        return ResponseEntity.ok(assigned);
    }
}
