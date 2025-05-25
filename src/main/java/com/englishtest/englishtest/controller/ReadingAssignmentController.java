package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.dto.ExcelReader;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.entity.reading.*;
import com.englishtest.englishtest.entity.writing.Submission;
import com.englishtest.englishtest.entity.writing.WritingAssignment;
import com.englishtest.englishtest.repository.ReadingAssignmentRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReadingAssignmentController {

    @Autowired
    private ReadingAssignmentRepository assignmentRepo;

    @Autowired
    private UserRepository userRepo;


    @GetMapping("/reading/assignments/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable String id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập");
        }

        Optional<ReadingAssignment> opt = assignmentRepo.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bài đọc");
        }

        ReadingAssignment assignment = opt.get();

        // Kiểm tra xem học sinh hiện tại có được giao bài không
        boolean assignedToThisStudent = assignment.getAssignedUserIds()
                .stream().anyMatch(uid -> uid.equals(principal.getName()));

//        if (!assignedToThisStudent) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không được giao bài này");
//        }

        // Trả về dữ liệu cần thiết
        Map<String, Object> result = new HashMap<>();
        result.put("id", assignment.getId());
        result.put("description", assignment.getDescription());
        result.put("pdfUrl", assignment.getReadingPdfUrl());
        result.put("totalQuestions", assignment.getAnswerKey().size());

        return ResponseEntity.ok(result);
    }


    // Tạo bài đọc mới (upload pdf + excel)
    @PostMapping("/reading/assignments")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> createReadingAssignment(
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam("excelFile") MultipartFile excelFile,
            @RequestParam("description") String description,
            @RequestParam("dueDate") String dueDateStr,
            @RequestParam("assignedUserIds") String assignedUserIdsJson
    ) {
        try {
            // Parse JSON string thành List<String>
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> assignedUserIds = objectMapper.readValue(assignedUserIdsJson, List.class);

            // Lưu file PDF và lấy URL
            String pdfUrl = saveFileAndGetUrl(pdfFile);

            // Đọc file Excel thành danh sách câu hỏi
            List<QuestionAnswer> answerKey = ExcelReader.readAnswerKeyFromExcel(excelFile.getInputStream());

            ReadingAssignment assignment = new ReadingAssignment();
            assignment.setDescription(description);
            assignment.setTimeCreated(LocalDate.now());
            assignment.setDueDate(LocalDate.parse(dueDateStr));
            assignment.setReadingPdfUrl(pdfUrl);
            assignment.setAnswerKey(answerKey);

            // Lưu danh sách userId (tuỳ thuộc entity của bạn)
            assignment.setAssignedUserIds(assignedUserIds);

            assignmentRepo.save(assignment);

            return ResponseEntity.ok("Reading assignment created successfully");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid JSON or file input: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }


    // Lấy danh sách bài đọc assigned cho học sinh
    @GetMapping("/reading/assignments/mine")
    @PreAuthorize("hasRole('STUDENT')")
    public List<ReadingAssignment> getReadingAssignmentsForStudent() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(username);
        return assignmentRepo.findByAssignedUserIdsContains(student.getId());
    }

    // Học sinh nộp bài đọc
    @PostMapping("/reading/assignments/{id}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitReadingAssignment(
            @PathVariable String id,
            @RequestBody ReadingSubmission submissionRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(username);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }

        ReadingAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
        }

        boolean alreadySubmitted = assignment.getSubmissions().stream()
                .anyMatch(s -> s.getUserId().equals(student.getId()));

        if (alreadySubmitted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have already submitted this assignment");
        }

        // Tính điểm
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
        submissionRequest.setScore(correctCount);  // Gán điểm

        assignment.getSubmissions().add(submissionRequest);
        assignmentRepo.save(assignment);

        return ResponseEntity.ok("Submission successful. Score: " + correctCount);
    }


    // Giáo viên lấy bài nộp của học sinh
    @GetMapping("/reading/assignments/{assignmentId}/submission/{userId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<?> getStudentSubmission(
            @PathVariable String assignmentId,
            @PathVariable Long userId
    ) {
        ReadingAssignment assignment = assignmentRepo.findById(assignmentId).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assignment not found");
        }

        Optional<ReadingSubmission> submission = assignment.getSubmissions().stream()
                .filter(s -> s.getUserId().equals(userId))
                .findFirst();

        if (submission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission not found");
        }

        return ResponseEntity.ok(submission.get());
    }

    @GetMapping("/reading/assignments/{id}/submission")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getSubmission(@PathVariable String id) {
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepo.findByUsername(authenticationName);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Học sinh không tồn tại.");
        }

        ReadingAssignment assignment = assignmentRepo.findById(id).orElse(null);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bài đọc không tồn tại.");
        }

        Optional<ReadingSubmission> submission = assignment.getSubmissions().stream()
                .filter(sub -> sub.getUserId().equals(student.getId()))
                .findFirst();

        if (submission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bạn chưa nộp bài này.");
        }

        return ResponseEntity.ok(submission.get());
    }




    private final String UPLOAD_DIR = "D:/englishtest/uploads";

    private String saveFileAndGetUrl(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return "/uploads/" + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
