package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.Course;
import com.englishtest.englishtest.entity.ReadingTest;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.repository.CourseRepository;
import com.englishtest.englishtest.repository.ReadingTestRepository;
import com.englishtest.englishtest.service.CourseService;
import com.englishtest.englishtest.service.itf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReadingTestRepository readingTestRepository;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{courseId}/students")
    public List<User> getStudentsByCourse(@PathVariable Long courseId) {
        return courseRepository.findById(courseId)
                .map(Course::getUsers)
                .orElse(Collections.emptyList());
    }


    @GetMapping("/{id}")
    public Optional<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsersForCourse(@PathVariable Long id) {
        List<User> users = courseService.getUsersForCourse(id);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/users")
    public ResponseEntity<Course> updateCourseUsers(@PathVariable Long id, @RequestBody List<Long> userIds) {
        Course course = courseService.updateCourseUsers(id, userIds);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourseName(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (!courseOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        Course course = courseOpt.get();
        String newName = body.get("name");
        if (newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }

        course.setName(newName);
        courseRepository.save(course);

        return ResponseEntity.ok(course);
    }

    @PutMapping("/{courseId}/reading-tests")
    public ResponseEntity<?> assignReadingTestsToCourse(
            @PathVariable Long courseId,
            @RequestBody List<Long> readingTestIds) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<ReadingTest> readingTests = readingTestRepository.findAllById(readingTestIds);

        course.setReadingTests(readingTests);
        courseRepository.save(course);

        return ResponseEntity.ok(course);
    }
}
